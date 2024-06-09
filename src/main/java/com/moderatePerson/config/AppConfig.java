package com.moderatePerson.config;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class AppConfig {

    @Value("${app.chat.dev.url}")
    private String devApiUrl;
    @Value("${app.chat.dev.key}")
    private String devPersonalAccessToken;
    @Value("${app.chat.dev.bot_id}")
    private String devBotId;

    @Value("${app.chat.test.url}")
    private String testApiUrl;
    @Value("${app.chat.test.key}")
    private String testPersonalAccessToken;
    @Value("${app.chat.test.bot_id}")
    private String testBotId;

    @Value("${app.chat.prod.url}")
    private String prodApiUrl;
    @Value("${app.chat.prod.key}")
    private String prodPersonalAccessToken;
    @Value("${app.chat.prod.bot_id}")
    private String prodBotId;

    // Getters for dev environment
    public String getDevApiUrl() {
        return devApiUrl;
    }
    public String getDevPersonalAccessToken() {
        return devPersonalAccessToken;
    }
    public String getDevBotId() {
        return devBotId;
    }

    // Getters for test environment
    public String getTestApiUrl() {
        return testApiUrl;
    }
    public String getTestPersonalAccessToken() {
        return testPersonalAccessToken;
    }
    public String getTestBotId() {
        return testBotId;
    }

    // Getters for prod environment
    public String getProdApiUrl() {
        return prodApiUrl;
    }
    public String getProdPersonalAccessToken() {
        return prodPersonalAccessToken;
    }
    public String getProdBotId() {
        return prodBotId;
    }

    @Bean
    public RestTemplate restTemplate() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Create an SSL context that uses the trust manager
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial((chain, authType) -> true)
                    .build();

            // Create an HttpClient that uses the SSL context
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier((hostname, session) -> true)
                    .build();

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            return new RestTemplate(factory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Ensure that the RestTemplate uses UTF-8 encoding
//        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
//        for (HttpMessageConverter<?> converter : converters) {
//            if (converter instanceof StringHttpMessageConverter) {
//                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
//            }
//        }
//
//        return restTemplate;
//    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
//        // 没有 quoteNonAscii 方法，确保其他配置项设置正确
//        return objectMapper;
//    }
}

