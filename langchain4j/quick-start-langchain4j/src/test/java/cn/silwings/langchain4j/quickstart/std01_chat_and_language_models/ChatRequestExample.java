package cn.silwings.langchain4j.quickstart.std01_chat_and_language_models;

import cn.silwings.aidemo.common.utils.Env;
import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;

import java.util.List;

public class ChatRequestExample {

    public static void main(String[] args) {
        final ChatModel model = QwenChatModel.builder()
                .apiKey(Env.ALI_AI_KEY.get())
                .modelName(Model.QWEN.QWEN_MAX)
                .build();

        ChatRequest request = ChatRequest.builder()
                .messages(UserMessage.from("hello"))
                .parameters(ChatRequestParameters.builder()
                        .temperature(0.5)
                        .toolSpecifications(List.of())
                        .build())
                .build();

        final ChatResponse response = model.chat(request);
        System.out.println(response.aiMessage().text());
        System.out.println("输入token数: " + response.metadata().tokenUsage().inputTokenCount());
        System.out.println("输出token数: " + response.metadata().tokenUsage().outputTokenCount());
        System.out.println("总token数: " + response.metadata().tokenUsage().totalTokenCount());
    }

}
