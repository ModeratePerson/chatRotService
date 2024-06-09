package com.moderatePerson.utils.coze;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moderatePerson.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestBuilder {
    @Autowired
    private AppConfig appConfig;

    public HttpEntity<MultiValueMap<String, String>> buildRequest(String query, String botId, String personalAccessToken, String uuid, String conversationId, String chatHistory) {
        // 构造请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + personalAccessToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Connection", "Keep-alive");
        headers.set("Accept", "*/*");

        // 构造请求体
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("bot_id", botId);
        body.add("stream", "true");
        body.add("query", query);
        body.add("user", uuid);
        body.add("conversation_id", conversationId);
        body.add("chat_history", chatHistory);
//        body.add("chat_history", chatHistory);

        return new HttpEntity<>(body, headers);
    }
    public String buildQueryString(MultiValueMap<String, String> params) {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String key = entry.getKey();
            for (String value : entry.getValue()) {
                if (queryString.length() > 0) {
                    queryString.append("&");
                }
                queryString.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
                queryString.append("=");
                queryString.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            }
        }
        return queryString.toString();
    }
    public static HttpHeaders createHeaders(String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        headers.setBearerAuth(bearerToken);
        headers.setConnection("keep-alive");
        return headers;
    }
    public static byte[] createRequestBody(String conversationId, String botId, String user, String query, boolean stream,String chatHistory) throws JsonProcessingException, JsonProcessingException {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("conversation_id", conversationId);
        bodyMap.put("bot_id", botId);
        bodyMap.put("user", user);
        bodyMap.put("query", query);
        bodyMap.put("stream", stream);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(bodyMap);
    }

}
