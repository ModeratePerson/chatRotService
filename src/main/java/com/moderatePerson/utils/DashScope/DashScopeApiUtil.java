//package com.moderatePerson.utils.DashScope;
//
//import com.moderatePerson.config.DashScopeConfig;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//
//@Component
//@Configuration
//@ConfigurationProperties(prefix = "dash-scope")
//public class DashScopeApiUtil {
//
//    private static final String USER_AGENT = "Java-HttpURLConnection/1.0";
//    @Autowired
//    private DashScopeConfig dashScopeConfig;
//    // 单次对话
//    public String singleConversation(String modelName, String userInput) throws Exception {
//        URL url = new URL(dashScopeConfig.getUrl());
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestProperty("Authorization", "Bearer " + dashScopeConfig.getKey());
//        connection.setDoOutput(true);
//
//        String jsonInputString = String.format("{\"model\": \"%s\", \"input\": {\"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"user\", \"content\": \"%s\"}]}, \"parameters\": {\"result_format\": \"message\"}}", modelName, userInput);
//
//
//        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
//            wr.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
//            wr.flush();
//        }
//
//        StringBuilder response = new StringBuilder();
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//        }
//
//        connection.disconnect();
//        return response.toString();
//    }
//    // 对话集, 包含上下文对话
//    private JSONArray messages = new JSONArray();
//
//    public DashScopeApiUtil() {
//        // Initialize with a system message
//        JSONObject systemMessage = new JSONObject();
//        systemMessage.put("role", "system");
//        systemMessage.put("content", "You are a helpful assistant.");
//        messages.put(systemMessage);
//    }
//
//    public String handleUserInput(String modelName,String userInput) throws Exception {
//        // Add user message
//        JSONObject userMessage = new JSONObject();
//        userMessage.put("role", "user");
//        userMessage.put("content", userInput);
//        messages.put(userMessage);
//
//        // Prepare the request body
//        JSONObject body = new JSONObject();
//        JSONObject inputMessage = new JSONObject();
//        inputMessage.put("messages", messages);
//        JSONObject parametersMessage = new JSONObject();
//        parametersMessage.put("result_format", "message");
//
//        body.put("model",modelName);
//        body.put("input", inputMessage);
//        body.put("parameters", parametersMessage);
//
//        // Get response from the API
//        JSONObject assistantOutput = multipleConversation(body);
//
//        // Extract and return the content
//        JSONObject output = assistantOutput.getJSONObject("output");
//        JSONArray choices = output.getJSONArray("choices");
//        JSONObject firstChoice = choices.getJSONObject(0);
//        JSONObject assistantMessage = firstChoice.getJSONObject("message");
//        String content = assistantMessage.getString("content");
//
//        // Add assistant message to messages
//        messages.put(assistantMessage);
//
//        return content;
//    }
//    // 多轮对话
//    public JSONObject multipleConversation(JSONObject body) throws Exception {
//        URL url = new URL(dashScopeConfig.getUrl());
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        // 设置 SSL/TLS 版本
//        connection.setRequestProperty("https.protocols", "TLSv1.2");
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestProperty("Authorization", "Bearer " + dashScopeConfig.getKey());
//        connection.setDoOutput(true);
//
//        String jsonInputString = body.toString();
//        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
//            wr.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
//            wr.flush();
//        }
//
//        StringBuilder response = new StringBuilder();
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//        }
//
//        JSONObject responseJson = new JSONObject(response.toString());
//        connection.disconnect();
//        return responseJson;
//    }
//}
