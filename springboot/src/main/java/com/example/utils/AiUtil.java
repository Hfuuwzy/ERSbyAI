package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * AI工具类
 * 用于调用 kimi-k2.6 API 进行简历筛选和评估
 */
@Component
public class AiUtil {

    @Value("${ai.api-key}")
    private String apiKey;

    @Value("${ai.model:kimi-k2.6}")
    private String model;

    @Value("${ai.base-url:https://kimi.a7m.com.cn/v1}")
    private String baseUrl;

    private final OkHttpClient client;
    private final Gson gson;

    public AiUtil() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }

    /**
     * 调用 AI 服务进行对话
     *
     * @param list 对话消息列表
     * @return AI 返回的文本内容
     */
    public String ai(List<String> list) {
        // 验证配置
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("AI API Key 未配置。请在 application-local.yml 中配置 ai.api-key 或设置 KIMI_API_KEY 环境变量。");
        }

        // 构建请求体
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model);

        JsonArray messages = new JsonArray();
        for (String content : list) {
            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", content);
            messages.add(message);
        }
        requestBody.add("messages", messages);

        // 构建 HTTP 请求
        Request request = new Request.Builder()
                .url(baseUrl + "/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();

        // 发送请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                throw new RuntimeException("AI API 请求失败: HTTP " + response.code() + " - " + errorBody);
            }

            String responseBody = response.body().string();
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            JsonArray choices = jsonResponse.getAsJsonArray("choices");
            if (choices == null || choices.size() == 0) {
                throw new RuntimeException("AI API 返回空 choices");
            }

            JsonObject firstChoice = choices.get(0).getAsJsonObject();
            JsonObject message = firstChoice.getAsJsonObject("message");
            return message.get("content").getAsString().trim();

        } catch (IOException e) {
            throw new RuntimeException("AI 服务调用失败: " + e.getMessage(), e);
        }
    }
}
