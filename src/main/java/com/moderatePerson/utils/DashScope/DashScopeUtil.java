package com.moderatePerson.utils.DashScope;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moderatePerson.config.DashScopeConfig;
import jep.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class DashScopeUtil {
    @Autowired
    private DashScopeConfig dashScopeConfig;

    private List<Map<String, String>> messages = new ArrayList<>();
    static {
        // 设置JEP库路径
//        System.setProperty("java.library.path", "C:\\Program Files\\Python311\\Lib\\site-packages\\jep");
        // 反射来刷新类加载器
//        try {
//            final java.lang.reflect.Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
//            sysPathsField.setAccessible(true);
//            sysPathsField.set(null, null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void DashscopeService() {
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");
        messages.add(systemMessage);
    }

    public String getAssistantResponse(String userInput) throws JepException {
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userInput);
        messages.add(userMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        String messagesJson;
        try {
            messagesJson = objectMapper.writeValueAsString(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error serializing messages: " + e.getMessage();
        }

        try (SharedInterpreter interp = new SharedInterpreter()) {
            interp.exec("import os");
            interp.exec("os.environ['DASHSCOPE_API_KEY'] = '" + dashScopeConfig.getKey() + "'");
            interp.exec("import sys");
            interp.exec("sys.path.append('src/main/java/com/moderatePerson/utils/DashScope/py')");  // 添加Python脚本路径
            interp.exec("import json");
            interp.exec("from DashScope import get_response");

            interp.set("messages_json", messagesJson);
            interp.exec("messages = json.loads(messages_json)");
            interp.exec("response = get_response(messages)");
            String responseJson = (String) interp.getValue("json.dumps(response)");

            JsonNode responseNode = objectMapper.readTree(responseJson);
            JsonNode choicesNode = responseNode.path("output").path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).path("message");
                if (messageNode != null && messageNode.has("content")) {
                    String assistantResponse = messageNode.get("content").asText();
                    Map<String, String> assistantMessage = new HashMap<>();
                    assistantMessage.put("role", "assistant");
                    assistantMessage.put("content", assistantResponse);
                    messages.add(assistantMessage);
                    return assistantResponse;
                }
            }
            return "No valid response found.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
