package com.example.utils;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * AI工具类
 * 用于调用火山引擎 DeepSeek API 进行简历筛选和评估
 */
@Component
public class AiUtil {

    @Value("${ai.api-key}")
    private String apiKey;

    @Value("${ai.model:deepseek-r1-250120}")
    private String model;

    /**
     * 调用 AI 服务进行对话
     *
     * @param list 对话消息列表
     * @return AI 返回的文本内容
     */
    public String ai(List<String> list) {
        // 验证 API Key 是否配置
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("AI API Key 未配置。请在 application-local.yml 中配置 ai.api-key 或设置 AI_API_KEY 环境变量。");
        }

        StringBuilder sb = new StringBuilder();
        ArkService arkService = ArkService.builder().apiKey(apiKey).build();

        try {
            List<ChatMessage> chatMessages = new ArrayList<>();
            for (String message : list) {
                ChatMessage userMessage = ChatMessage.builder()
                        .role(ChatMessageRole.USER)
                        .content(message)
                        .build();
                chatMessages.add(userMessage);
            }

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(chatMessages)
                    .build();

            arkService.createChatCompletion(chatCompletionRequest)
                    .getChoices()
                    .forEach(choice -> sb.append(choice.getMessage().getContent()));

        } catch (Exception e) {
            System.err.println("AI 请求失败: " + e.getMessage());
            throw new RuntimeException("AI 服务调用失败: " + e.getMessage(), e);
        } finally {
            arkService.shutdownExecutor();
        }

        return sb.toString();
    }
}
