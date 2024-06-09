package com.moderatePerson.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
// 管理聊天记录，在同一对话中作为上下文
@Service
public class ChatHistoryService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CHAT_HISTORY_PREFIX = "chat_history:";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String createChatHistory(String conversationId, String userId, String module) throws IOException {
        String chatHistoryFilename = conversationId + "_chat_history.json";
        String chatHistoryFilePath = "src/main/resources/chathistory" + "/" + userId + "/" + module + "/" + chatHistoryFilename;

        File chatHistoryFile = new File(chatHistoryFilePath);
        chatHistoryFile.getParentFile().mkdirs();
        chatHistoryFile.createNewFile();

        // 初始化 chat_history 文件内容
        try (FileWriter writer = new FileWriter(chatHistoryFile)) {
            writer.write("[]");
        }

        // 存储文件路径到 Redis
        redisTemplate.opsForValue().set(CHAT_HISTORY_PREFIX + module + ":" + userId, chatHistoryFilePath);

        return chatHistoryFilePath;
    }

    public String getChatHistoryFilePath(String userId, String module) {
        return redisTemplate.opsForValue().get(CHAT_HISTORY_PREFIX + module + ":" + userId);
    }
    @Async
    public void updateChatHistory(String userId, String module, List<Map<String, Object>> newEntries) throws IOException {
        String chatHistoryFilePath = getChatHistoryFilePath(userId, module);

        if (chatHistoryFilePath != null) {
            String content = new String(Files.readAllBytes(Paths.get(chatHistoryFilePath)));
            List<Map<String, Object>> chatHistory = objectMapper.readValue(content, new TypeReference<List<Map<String, Object>>>() {});
            chatHistory.addAll(newEntries);
            Files.write(Paths.get(chatHistoryFilePath), objectMapper.writeValueAsBytes(chatHistory));
        }
    }
    @Async
    public void updateLastEntryContent(String userId, String module, String newContent) {
        String chatHistoryFilePath = getChatHistoryFilePath(userId, module);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 读取现有的chat_history JSON文件
            List<Map<String, Object>> chatHistory = objectMapper.readValue(new File(chatHistoryFilePath), new TypeReference<List<Map<String, Object>>>() {});

            // 检查chatHistory是否为空
            if (!chatHistory.isEmpty()) {
                // 获取列表中最后一个字典
                Map<String, Object> lastEntry = chatHistory.get(chatHistory.size() - 1);

                // 更新content字段
                lastEntry.put("content", newContent);

                // 将更新后的chat_history写回文件
                objectMapper.writeValue(new File(chatHistoryFilePath), chatHistory);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 处理可能的IO异常
        }
    }
}

