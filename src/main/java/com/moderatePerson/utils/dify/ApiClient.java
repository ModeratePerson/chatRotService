package com.moderatePerson.utils.dify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

@Component
public class ApiClient {

    private final WebClient webClient;
    private final RedisTemplate<String, String> redisTemplate;
    @Autowired
    public ApiClient(WebClient.Builder webClientBuilder,RedisTemplate<String, String> redisTemplate) {
        this.webClient = webClientBuilder.baseUrl("https://api.dify.ai/v1/chat-messages").build();
        this.redisTemplate = redisTemplate;
    }

    public Flux<String> sendStreamingRequest(String query,String uuid) {
        String conversation_id = redisTemplate.opsForValue().get(uuid); // 从redis中取出聊天id
        String body = "{\n" +
                "    \"inputs\": {},\n" +
                "    \"query\": \"" + query + "\",\n" +
                "    \"response_mode\": \"streaming\",\n" +
                "    \"conversation_id\": \""+ conversation_id +"\",\n" +
                "    \"user\": \""+ uuid +"\",\n" +
                "    \"files\": [\n" +
                "      {\n" +
                "        \"type\": \"image\",\n" +
                "        \"transfer_method\": \"remote_url\",\n" +
                "        \"url\": \"https://cloud.dify.ai/logo/logo-site.png\"\n" +
                "      }\n" +
                "    ]\n" +
                "}";
        return webClient.post()
                .uri("https://api.dify.ai/v1/chat-messages") // 替换为实际的URI
                .header(HttpHeaders.AUTHORIZATION, "Bearer app-z9VC7dLGuZsTtl4qW9TavpUG")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(String.class);
    }
}
