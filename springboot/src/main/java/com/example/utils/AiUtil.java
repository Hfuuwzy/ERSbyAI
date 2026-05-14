package com.example.utils;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;

import java.util.ArrayList;
import java.util.List;

/**
 * @version: 1.00.00
 * @description: Ai工具类
 * @date: 2025/5/9 22:06
 */
public class AiUtil {

    public static String ai(List<String> list){
        StringBuilder sb = new StringBuilder();
        // 从环境变量中获取API密钥
        String apiKey = "eb9000bd-1357-4635-a826-f11920f596ef";
        // 创建ArkService实例
        ArkService arkService = ArkService.builder().apiKey(apiKey).build();
        // 初始化消息列表
        List<ChatMessage> chatMessages = new ArrayList<>();
        // 将用户消息添加到消息列表
        for (int i=0;i<list.size();i++){
            ChatMessage userMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER) // 设置消息角色为用户
                    .content(list.get(i)) // 设置消息内容
                    .build();
            chatMessages.add(userMessage);
        }
        // 创建聊天完成请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("deepseek-r1-250120")// 需要替换为Model ID
                .messages(chatMessages) // 设置消息列表
                .build();
        // 发送聊天完成请求并打印响应
        try {
            // 获取响应并打印每个选择的消息内容
            arkService.createChatCompletion(chatCompletionRequest)
                    .getChoices()
                    .forEach(choice ->sb.append(choice.getMessage().getContent()));
        } catch (Exception e) {
            System.out.println("请求失败: " + e.getMessage());
        } finally {
            // 关闭服务执行器
            arkService.shutdownExecutor();
        }

        return sb.toString();
    }

}
