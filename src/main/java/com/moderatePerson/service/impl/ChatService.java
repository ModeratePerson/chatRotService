package com.moderatePerson.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moderatePerson.utils.coze.RequestBuilder;
import org.apache.tomcat.util.buf.CharChunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatService {
    @Autowired
    private RequestBuilder requestBuilder;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    public SseEmitter chatWithBot(String clientId, String query, MultipartFile file, String apiUrl, String botId, String personalAccessToken, String userId, String conversationId, String module) throws IOException {
//        RestTemplate restTemplate = new RestTemplate();
        // 上传文件并获取文件URL
        String fileUrl = fileUploadService.uploadFile(file);

        // 构造包含文件URL的查询文本
        String updatedQuery = query + " " + fileUrl;

        SseEmitter emitter = new SseEmitter();
        emitters.put(clientId, emitter);
        emitter.onCompletion(() -> emitters.remove(clientId));
        emitter.onTimeout(() -> emitters.remove(clientId));
        emitter.onError((e) -> emitters.remove(clientId));

        // 加载 chat_history
        String chatHistoryFilePath = chatHistoryService.getChatHistoryFilePath(userId, module);
        String chatHistoryContent = new String(Files.readAllBytes(Paths.get(chatHistoryFilePath)));
        List<Map<String, Object>> chatHistory = objectMapper.readValue(chatHistoryContent, new TypeReference<List<Map<String, Object>>>() {});
        List<Map<String,Object>> newChatHistory = new ArrayList<>();

        // 将请求内容添加到 chat_history
        Map<String, Object> userEntry = new HashMap<>();
        userEntry.put("role", "user");
        userEntry.put("content", updatedQuery);
        userEntry.put("content_type", "text");
        newChatHistory.add(userEntry);

        // 更新用户提问的chat_history
        chatHistoryService.updateChatHistory(userId, module, newChatHistory);

//        List<Map<String,Object>> assistantChatHistory = new ArrayList<>();
//        Map<String,Object> assistantEntry = new HashMap<>();
//        assistantEntry.put("role","assistant");
//        assistantEntry.put("content","");
//        assistantEntry.put("content_type","text");
//        assistantEntry.put("type","answer");
//        assistantChatHistory.add(assistantEntry);
//        // 更新助手chat_history,不包含content
//        chatHistoryService.updateChatHistory(userId,module,assistantChatHistory);

        // 使用 RequestBuilder 构造请求体
//        HttpEntity<MultiValueMap<String, String>> entity = requestBuilder.buildRequest(query, botId, personalAccessToken, userId, conversationId, objectMapper.writeValueAsString(chatHistory));

        // 解决中文乱码
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter converter : list) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(Charset.forName("UTF-8"));
                break;
            }
        }
        // 请求体构造
        HttpHeaders headers = RequestBuilder.createHeaders(personalAccessToken);
        byte[] body = RequestBuilder.createRequestBody(conversationId, botId, userId, updatedQuery, true,chatHistoryContent);

        // 使用SseEmitter实时推送数据
        new Thread(() -> {
            try {
                // 捕获整个响应体
                ByteArrayOutputStream responseBodyStream = new ByteArrayOutputStream();
                restTemplate.execute(apiUrl, HttpMethod.POST,
                        request -> {
                            request.getHeaders().addAll(headers);
                            request.getBody().write(body);
                        },
                        (ResponseExtractor<Void>) response -> {
                            String line;
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                                while ((line = reader.readLine()) != null) {

                                    // 检查连接是否存在
                                    if (!emitters.containsKey(clientId)) {
                                        break;
                                    }

                                    // 发送数据到客户端
                                    emitter.send(line);
                                    // 将每行数据写入到 ByteArrayOutputStream
                                    responseBodyStream.write((line + "\n").getBytes());

                                }
                                emitter.onCompletion(() -> {
                                    String responseBody = responseBodyStream.toString();
                                    List<Map<String, Object>> entry = null;
                                    try {
                                        entry = extractResponseEntries(responseBody);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    try {
                                        chatHistoryService.updateChatHistory(userId,module,entry);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } catch (IOException ex) {
                                emitter.completeWithError(ex);
                            }
                            return null;
                        }
                );
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }

    public  SseEmitter chatWithBot(String clientId, String query, String apiUrl, String botId, String personalAccessToken, String userId, String conversationId, String module) throws IOException {
        SseEmitter emitter = new SseEmitter();
        emitters.put(clientId, emitter);
        emitter.onCompletion(() -> emitters.remove(clientId));
        emitter.onTimeout(() -> emitters.remove(clientId));
        emitter.onError((e) -> emitters.remove(clientId));

        // 加载 chat_history
        String chatHistoryFilePath = chatHistoryService.getChatHistoryFilePath(userId, module);
        String chatHistoryContent = new String(Files.readAllBytes(Paths.get(chatHistoryFilePath)));
        List<Map<String, Object>> chatHistory = objectMapper.readValue(chatHistoryContent, new TypeReference<List<Map<String, Object>>>() {});
        List<Map<String,Object>> newChatHistory = new ArrayList<>();

        // 将请求内容添加到 chat_history
        Map<String, Object> userEntry = new HashMap<>();
        userEntry.put("role", "user");
        userEntry.put("content", query);
        userEntry.put("content_type", "text");
        newChatHistory.add(userEntry);

        // 更新用户提问的chat_history
        chatHistoryService.updateChatHistory(userId, module, newChatHistory);
//
//        List<Map<String,Object>> assistantChatHistory = new ArrayList<>();
//        Map<String,Object> assistantEntry = new HashMap<>();
//        assistantEntry.put("role","assistant");
//        assistantEntry.put("content","");
//        assistantEntry.put("content_type","text");
//        assistantEntry.put("type","answer");
//        assistantChatHistory.add(assistantEntry);
//        // 更新助手chat_history,不包含content
//        chatHistoryService.updateChatHistory(userId,module,assistantChatHistory);

        // 使用 RequestBuilder 构造请求体
//        HttpEntity<MultiValueMap<String, String>> entity = requestBuilder.buildRequest(query, botId, personalAccessToken, userId, conversationId, objectMapper.writeValueAsString(chatHistory));

        // 解决中文乱码
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        for (HttpMessageConverter converter : list) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(Charset.forName("UTF-8"));
                break;
            }
        }
        // 请求体构造
        HttpHeaders headers = RequestBuilder.createHeaders(personalAccessToken);
        byte[] body = RequestBuilder.createRequestBody(conversationId, botId, userId, query, true,chatHistoryContent);

        // 使用SseEmitter实时推送数据
        new Thread(() -> {
            try {
                // 捕获整个响应体
                ByteArrayOutputStream responseBodyStream = new ByteArrayOutputStream();
                restTemplate.execute(apiUrl, HttpMethod.POST,
                        request -> {
                            request.getHeaders().addAll(headers);
                            request.getBody().write(body);
                        },
                        (ResponseExtractor<Void>) response -> {
                            String line;
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                                while ((line = reader.readLine()) != null) {

                                    // 检查连接是否存在
                                    if (!emitters.containsKey(clientId)) {
                                        break;
                                    }

                                    // 发送数据到客户端
                                    emitter.send(line);
                                    // 将每行数据写入到 ByteArrayOutputStream
                                    responseBodyStream.write((line + "\n").getBytes());

                                }
                                emitter.onCompletion(() -> {
                                    String responseBody = responseBodyStream.toString();
                                    List<Map<String, Object>> entry = null;
                                    try {
                                        entry = extractResponseEntries(responseBody);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    try {
                                        chatHistoryService.updateChatHistory(userId,module,entry);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } catch (IOException ex) {
                                emitter.completeWithError(ex);
                          }
                            return null;
                        }
                );
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }
    @Async
    public List<Map<String,Object>> extractResponseEntries(String responseBody) throws IOException {
            List<Map<String, Object>> entries = new ArrayList<>();

            // 分割响应字符串，按行处理
            String[] responseLines = responseBody.split("\n");

//            Pattern jsonPattern = Pattern.compile("\\{[^{}]*\"event\"[^{}]*\"seq_id\"[^{}]*\\}");
        StringBuilder contentBuilder = new StringBuilder();
        for (String line : responseLines) {
//                Matcher matcher = jsonPattern.matcher(line);
//                String jsonResponse = "";
//                while (matcher.find()) {
//                    jsonResponse = matcher.group();;
//                }
                // 忽略不以 "data:" 开头的行
                if (!line.startsWith("data:")){
                    continue;
                }

                // 移除 "data:" 前缀
                String jsonResponse = line.substring(5);

                // 解析 JSON 响应
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);

                // 处理 "event" 为 "message" 的响应
                if (jsonNode.has("event") && "message".equals(jsonNode.get("event").asText())) {
                    JsonNode messageNode = jsonNode.get("message");

                    // 仅处理 type 为 "answer" 的消息
                    if (messageNode != null && "answer".equals(messageNode.get("type").asText())) {
                        // 拼接消息内容
                        contentBuilder.append(messageNode.get("content").asText());
                    }
                } else if (jsonNode.has("event") && "done".equals(jsonNode.get("event").asText())) {
                    // 遇到 "done" 事件时，表示消息已经结束
                    break;
                } else if (jsonNode.has("event") && "error".equals(jsonNode.get("event").asText())) {
                    // 处理错误事件
                    throw new RuntimeException("API Error: " + jsonNode.get("error_information").get("msg").asText());
                }
            }

            // 将拼接好的消息内容作为一个完整的条目添加到 chat_history
            if (contentBuilder.length() > 0) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("role", "assistant");
                entry.put("type", "answer");
                entry.put("content", contentBuilder.toString());
                entry.put("content_type", "text");
                entries.add(entry);
            }
        return entries;
    }
}
