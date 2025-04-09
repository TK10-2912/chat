package com.chatdemo.chatdemo.controller;

import com.chatdemo.chatdemo.Reponse.Chat.Group;
import com.chatdemo.chatdemo.entity.*;
import com.chatdemo.chatdemo.service.AuthService.AuthenticateService;
import com.chatdemo.chatdemo.service.ChatService.ChatService;
import com.corundumstudio.socketio.SocketIOServer;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private AuthenticateService userService;
    @Autowired
    private SocketIOServer socketIOServer;
    @PostMapping("/start-chat")
    public ResponseEntity<String> createPrivateChat(@RequestBody Map<String, String> request) {
        String userId1 = request.get("userId1");
        String userId2 = request.get("userId2");
        System.out.println(userId1 + " " + userId2);

        // Kiểm tra xem chat đã tồn tại chưa
        Query query = new Query(Criteria.where("type").is("PRIVATE")
                .and("participants.userId").all(userId1, userId2));
        Chat existingChat = mongoTemplate.findOne(query, Chat.class);

        if (existingChat != null) {
            return ResponseEntity.ok(existingChat.getId().toString());
        }

        // Tạo chat mới
        Chat chat = new Chat();
        chat.setType("PRIVATE");
        chat.setParticipants(Arrays.asList(
               new Participant(userId1),
                new Participant(userId2)
        ));
        chat.setListMessages(new ArrayList<>());
        chat.setCreatedAt(new Date());
        chat.setUpdatedAt(new Date());

        mongoTemplate.save(chat);

        return ResponseEntity.ok(chat.getId().toString());
    }
    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, String> request) {
        String chatId = request.get("chatId");
        String senderId = request.get("senderId");
        String content = request.get("content");

        if (chatId == null || senderId == null || content == null) {
            return ResponseEntity.badRequest().body("chatId, senderId, and content are required");
        }

        Query query = new Query(Criteria.where("_id").is(new ObjectId(chatId)));
        Chat chat = mongoTemplate.findOne(query, Chat.class);

        if (chat == null) {
            return ResponseEntity.badRequest().body("Chat not found");
        }

        Message message = new Message();
        message.setId(new ObjectId().toString());
        message.setSenderId(senderId);
        message.setContent(content);
        message.setTimestamp(new Date());
        message.setReactions(new HashMap<>());
        chat.getListMessages().add(message);
        chat.setUpdatedAt(new Date());
        mongoTemplate.save(chat);
        System.out.println("Sending message to room: messages_" + chatId);
        socketIOServer.getBroadcastOperations().sendEvent("message_" + chatId, message);
//        messagingTemplate.convertAndSend("/topic/messages/" + chatId, message);
        return ResponseEntity.ok("Message sent successfully");
    }
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String chatId) {

        Query query = new Query(Criteria.where("_id").is(new ObjectId(chatId)));
        Chat chat = mongoTemplate.findOne(query, Chat.class);

        if (chat == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(chat.getListMessages());
    }
    @PostMapping("/create-group")
    public ResponseEntity<String> createGroupChat(@RequestBody Map<String, Object> request) {
        String groupName = (String) request.get("groupName");
        String adminID = (String) request.get("adminID");
        List<String> userIds = (List<String>) request.get("userIds");

        if (groupName == null || userIds == null || userIds.isEmpty()) {
            return ResponseEntity.badRequest().body("groupName and userIds are required");
        }

        // Chuyển username thành ObjectId
        List<ObjectId> objectIds = userIds.stream()
                .map(username -> new ObjectId(username))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (objectIds.size() != userIds.size()) {
            return ResponseEntity.badRequest().body("Some users not found");
        }

        // Kiểm tra trùng lặp nhóm
        Query query = new Query(Criteria.where("type").is("GROUP")
                .and("groupInfo.name").is(groupName)
                .and("groupInfo.listMembers.userId").all(objectIds));
        Chat existingChat = mongoTemplate.findOne(query, Chat.class);

        if (existingChat != null) {
            return ResponseEntity.ok(existingChat.getId().toString());
        }

        // Tạo mới nhóm
        Chat chat = new Chat();
        chat.setType("GROUP");
        chat.setIdAdmin(adminID);
        List<PermissionGroup> listRoleGroup = new ArrayList<>(Arrays.asList(
                PermissionGroup.ALLOW_CHAT,
                PermissionGroup.ADD_MEMBER,
                PermissionGroup.DELETE_MESSAGE,
                PermissionGroup.REMOVE_MEMBER
        ));
        chat.setListPermissionGroup(listRoleGroup);
        chat.setGroupInfo(new GroupInfo(groupName, objectIds.stream()
                .map(userId -> new Member(userId, new Date()))
                .collect(Collectors.toList())));
        chat.setListMessages(new ArrayList<>());
        chat.setCreatedAt(new Date());
        chat.setUpdatedAt(new Date());

        mongoTemplate.save(chat);
        return ResponseEntity.ok(chat.getId().toString());
    }
    @GetMapping("/groups/{id}")
    public ResponseEntity<List<Group>> getGroups(@PathVariable String id) {
        List<Group> groups = chatService.getGroupsForUser(id);
        return ResponseEntity.ok(groups);
    }
    @PostMapping("/typing")
    public ResponseEntity<String> handleTyping(@RequestBody Map<String, String> request) {
        String chatId = request.get("chatId");
        String userId = request.get("userId");
        String username = request.get("username");

        if (chatId == null || userId == null || username == null) {
            System.out.println("Invalid typing request: chatId=" + chatId + ", userId=" + userId + ", username=" + username);
            return ResponseEntity.badRequest().body("chatId, userId, and username are required");
        }

        System.out.println("Received typing event: chatId=" + chatId + ", userId=" + userId + ", username=" + username);

        Map<String, Object> typingUpdate = new HashMap<>();
        typingUpdate.put("chatId", chatId);
        typingUpdate.put("userId", userId);
        typingUpdate.put("username", username);
        typingUpdate.put("action", "typing");

        socketIOServer.getBroadcastOperations().sendEvent("typing_" + chatId, typingUpdate);

        return ResponseEntity.ok("Typing event sent");
    }
    @PostMapping("/add-reaction")
    public ResponseEntity<String> addReaction(@RequestBody Map<String, String> request) {
        String chatId = request.get("chatId");
        String messageId = request.get("messageId");
        String userId = request.get("userId");
        String reactionType = request.get("reactionType");

        if (chatId == null || messageId == null || userId == null || reactionType == null) {
            System.out.println("Invalid add-reaction request: chatId=" + chatId + ", messageId=" + messageId + ", userId=" + userId + ", reactionType=" + reactionType);
            return ResponseEntity.badRequest().body("chatId, messageId, userId, and reactionType are required");
        }

        System.out.println("Received add-reaction request: chatId=" + chatId + ", messageId=" + messageId + ", userId=" + userId + ", reactionType=" + reactionType);

        Query chatQuery = new Query(Criteria.where("_id").is(new ObjectId(chatId)));
        Chat chat = mongoTemplate.findOne(chatQuery, Chat.class);

        if (chat == null) {
            System.out.println("Chat not found for chatId: " + chatId);
            return ResponseEntity.badRequest().body("Chat not found");
        }

        Message message = chat.getListMessages().stream()
                .filter(msg -> msg.getId().equals(messageId))
                .findFirst()
                .orElse(null);

        if (message == null) {
            System.out.println("Message not found for messageId: " + messageId);
            return ResponseEntity.badRequest().body("Message not found");
        }

        if (message.getReactions() == null) {
            message.setReactions(new HashMap<>());
        }

        Map<String, List<String>> reactions = message.getReactions();
        reactions.computeIfAbsent(reactionType, k -> new ArrayList<>());
        if (!reactions.get(reactionType).contains(userId)) {
            reactions.get(reactionType).add(userId);
        }

        chat.setUpdatedAt(new Date());
        mongoTemplate.save(chat);

        Map<String, Object> reactionUpdate = new HashMap<>();
        reactionUpdate.put("messageId", messageId);
        reactionUpdate.put("reactionType", reactionType);
        reactionUpdate.put("userId", userId);
        reactionUpdate.put("action", "add");

        System.out.println("Sending reaction update to room: reactions_" + chatId);
        socketIOServer.getBroadcastOperations().sendEvent("reaction_" + chatId, reactionUpdate);

        return ResponseEntity.ok("Reaction added successfully");
    }

    @PostMapping("/remove-reaction")
    public ResponseEntity<String> removeReaction(@RequestBody Map<String, String> request) {
        String chatId = request.get("chatId");
        String messageId = request.get("messageId");
        String userId = request.get("userId");
        String reactionType = request.get("reactionType");

        if (chatId == null || messageId == null || userId == null || reactionType == null) {
            System.out.println("Invalid remove-reaction request: chatId=" + chatId + ", messageId=" + messageId + ", userId=" + userId + ", reactionType=" + reactionType);
            return ResponseEntity.badRequest().body("chatId, messageId, userId, and reactionType are required");
        }

        System.out.println("Received remove-reaction request: chatId=" + chatId + ", messageId=" + messageId + ", userId=" + userId + ", reactionType=" + reactionType);

        Query chatQuery = new Query(Criteria.where("_id").is(new ObjectId(chatId)));
        Chat chat = mongoTemplate.findOne(chatQuery, Chat.class);

        if (chat == null) {
            System.out.println("Chat not found for chatId: " + chatId);
            return ResponseEntity.badRequest().body("Chat not found");
        }

        Message message = chat.getListMessages().stream()
                .filter(msg -> msg.getId().equals(messageId))
                .findFirst()
                .orElse(null);

        if (message == null) {
            System.out.println("Message not found for messageId: " + messageId);
            return ResponseEntity.badRequest().body("Message not found");
        }

        if (message.getReactions() == null) {
            message.setReactions(new HashMap<>());
        }

        Map<String, List<String>> reactions = message.getReactions();
        if (reactions.containsKey(reactionType)) {
            reactions.get(reactionType).remove(userId);
            if (reactions.get(reactionType).isEmpty()) {
                reactions.remove(reactionType);
            }
        }

        chat.setUpdatedAt(new Date());
        mongoTemplate.save(chat);

        Map<String, Object> reactionUpdate = new HashMap<>();
        reactionUpdate.put("messageId", messageId);
        reactionUpdate.put("reactionType", reactionType);
        reactionUpdate.put("userId", userId);
        reactionUpdate.put("action", "remove");

        System.out.println("Sending reaction update to room: reactions_" + chatId);
        socketIOServer.getBroadcastOperations().sendEvent("reaction_" + chatId, reactionUpdate);

        return ResponseEntity.ok("Reaction removed successfully");
    }
    @GetMapping("/chats/{userId}")
    public ResponseEntity<List<String>> getChatsByUserId(@PathVariable String userId) {
        List<ObjectId> chatIds = chatService.getChatsByUserId(userId);
        List<Group> groups = chatService.getGroupsForUser(userId);
        if (chatIds.isEmpty() && groups.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 nếu không có dữ liệu
        }
        List<String> chatID = chatIds.stream()
                .map(ObjectId::toString) // Chuyển ObjectId thành String
                .collect(Collectors.toList());
        groups.forEach(group -> {
            chatID.add(group.getChatId());
        });
        return ResponseEntity.ok(chatID); // 200 kèm danh sách chatId
    }
}
