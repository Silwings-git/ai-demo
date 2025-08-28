package cn.silwings.langchain4j.quickstart.std01_chat_and_language_models;

import cn.silwings.aidemo.common.utils.Env;
import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;

public class MultiMessageExample {
    public static void main(String[] args) {
        final ChatModel model = QwenChatModel.builder()
                .apiKey(Env.ALI_AI_KEY.get())
                .modelName(Model.QWEN.QWEN_MAX)
                .build();

        final UserMessage firstUserMessage = UserMessage.from("Hello, my name is Silwings");
        final AiMessage firstAiMessage = model.chat(firstUserMessage).aiMessage();
        final UserMessage secondUserMessage = UserMessage.from("What is my name?");
        final AiMessage secondAiMessage = model.chat(firstUserMessage, firstAiMessage, secondUserMessage).aiMessage();

        System.out.println("Silwings: " + firstUserMessage.singleText());
        System.out.println("Qwen:     " + firstAiMessage.text());
        System.out.println("Silwings: " + secondUserMessage.singleText());
        System.out.println("Qwen:     " + secondAiMessage.text());
    }
}
