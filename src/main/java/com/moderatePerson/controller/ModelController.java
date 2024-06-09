package com.moderatePerson.controller;


import com.moderatePerson.config.AppConfig;
import com.moderatePerson.domain.pojo.Message;
import com.moderatePerson.service.impl.ChatHistoryService;
import com.moderatePerson.service.impl.ChatService;
import com.moderatePerson.service.impl.ConversationService;
import com.moderatePerson.utils.dify.ApiClient;
import com.moderatePerson.utils.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ApiClient apiClient;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private ChatService chatService;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private ChatHistoryService chatHistoryService;
    // 访问API获取流式响应
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String query, HttpServletRequest request) {
        String userId = JwtUtil.getUserId(request);// 获取userId
        String conversation_id = userId + System.currentTimeMillis(); // 根据userId和时间戳生成聊天id
        if (userId != null) {
            redisTemplate.opsForValue().set(userId, conversation_id);// 将uuid和conversation_id存在redis中，时间设为永久
        }
        return apiClient.sendStreamingRequest(query,userId);
    }
    // 生成新的聊天,旧的聊天不会保存,前端应该将原有聊天记录清空
    @GetMapping("/convert")
    public ResponseEntity<Message> convert(HttpServletRequest request){
        String userId = JwtUtil.getUserId(request);// 获取userId
        String conversation_id = userId + System.currentTimeMillis(); // 根据userId和时间戳生成聊天id
        if (userId != null) {
            redisTemplate.opsForValue().set(userId, conversation_id);// 将uuid和conversation_id存在redis中，时间设为永久
        }
        Message message = new Message();
        message.setStatus(200);
        message.setMsg("已生成新的聊天");
        return ResponseEntity.ok().body(message);
    }

    /**
        调用coze API
     */
    @PostMapping("/chat/work")
    public SseEmitter chatWithBotWork(HttpServletRequest request, @RequestParam String clientId, @RequestParam String query,
                                      @RequestParam(required = false) MultipartFile file) throws IOException {
        String userId = JwtUtil.getUserId(request);
        String conversationId = conversationService.getCurrentConversationId(userId, "work");

        if (conversationId == null) {
            conversationId = conversationService.startNewConversation(userId, "work");
            chatHistoryService.createChatHistory(conversationId, userId, "work");
        }

//        if (file != null && !file.isEmpty()) {
//            return chatService.chatWithBot(clientId, query, file, appConfig.getDevApiUrl(), appConfig.getDevBotId(), appConfig.getDevPersonalAccessToken(), userId, conversationId, "work");
//        } else {
            return chatService.chatWithBot(clientId, query, appConfig.getDevApiUrl(), appConfig.getDevBotId(), appConfig.getDevPersonalAccessToken(), userId, conversationId, "work");
//        }
    }

    @GetMapping("/chat/study")
    public SseEmitter chatWithBotStudy(HttpServletRequest request,@RequestParam String clientId, @RequestParam String query,
                                                   @RequestParam(required = false) MultipartFile file) throws IOException {
        String userId = JwtUtil.getUserId(request);
        String conversationId = conversationService.getCurrentConversationId(userId, "study");

        if (conversationId == null) {
            conversationId = conversationService.startNewConversation(userId, "study");
            chatHistoryService.createChatHistory(conversationId, userId, "study");
        }

//        if (file != null && !file.isEmpty()) {
//            return chatService.chatWithBot(clientId, query, file, appConfig.getTestApiUrl(), appConfig.getTestBotId(), appConfig.getTestPersonalAccessToken(), userId, conversationId, "study");
//        } else {
            return chatService.chatWithBot(clientId, query, appConfig.getTestApiUrl(), appConfig.getTestBotId(), appConfig.getTestPersonalAccessToken(), userId, conversationId, "study");
//        }
    }

    @PostMapping("/chat/life")
    public SseEmitter chatWithBotLife(HttpServletRequest request, @RequestParam String clientId, @RequestParam String query,
                                                  @RequestParam(required = false) MultipartFile file) throws IOException {
        String userId = JwtUtil.getUserId(request);
        String conversationId = conversationService.getCurrentConversationId(userId, "life");

        if (conversationId == null) {
            conversationId = conversationService.startNewConversation(userId, "life");
            chatHistoryService.createChatHistory(conversationId, userId, "life");
        }

//        if (file != null && !file.isEmpty()) {
//            return chatService.chatWithBot(clientId, query, file, appConfig.getProdApiUrl(), appConfig.getProdBotId(), appConfig.getProdPersonalAccessToken(), userId, conversationId, "life");
//        } else {
            return chatService.chatWithBot(clientId, query, appConfig.getProdApiUrl(), appConfig.getProdBotId(), appConfig.getProdPersonalAccessToken(), userId, conversationId, "life");
//        }
    }
    // 三种模块切换聊天
    @PostMapping("/chat/convert/{module}")
    public ResponseEntity<?> startNewConversation(HttpServletRequest request, @PathVariable String module) {
        String userId = JwtUtil.getUserId(request);
        String newConversationId = conversationService.startNewConversation(userId, module);
        try {
            chatHistoryService.createChatHistory(newConversationId,userId,module);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Message message = new Message();
        message.setStatus(200);
        message.setData(newConversationId);
        return ResponseEntity.ok().body(message);
    }
    // 返回三种模式聊天记录
    @PostMapping("/chat/history/{module}")
    public ResponseEntity<?> chatHistory(@PathVariable String module, HttpServletRequest request){
        String userId = JwtUtil.getUserId(request);
        String chatHistoryFilePath = chatHistoryService.getChatHistoryFilePath(userId, module);
        String chatHistoryContent = "";
        try {
             chatHistoryContent = new String(Files.readAllBytes(Paths.get(chatHistoryFilePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Message message = new Message();
        message.setStatus(200);
        message.setData(chatHistoryContent);
        return ResponseEntity.ok().body(message);
    }
}
