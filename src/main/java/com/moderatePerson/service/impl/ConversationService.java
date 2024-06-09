package com.moderatePerson.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
// 管理三种模块中的conversation_id
@Service
public class ConversationService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String PREFIX = "conversation_id:";

    public String getCurrentConversationId(String userId, String module) {
        return redisTemplate.opsForValue().get(PREFIX + module + ":" + userId);
    }

    public String startNewConversation(String userId, String module) {
        String newConversationId = userId + "_" + System.currentTimeMillis();
        redisTemplate.opsForValue().set(PREFIX + module + ":" + userId, newConversationId);
        return newConversationId;
    }
}

