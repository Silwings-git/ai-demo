package cn.silwings.langchain4j.quickstart;

import cn.silwings.aidemo.common.utils.Env;
import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * {@link <a href="https://docs.langchain4j.info/tutorials/chat-and-language-models">聊天和语言模型</a>}
 */
public class C01ChatAndLanguageModelsTest {

    @Test
    public void testChatRequest() {
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

    @Test
    public void testMultiMessage() {
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

    @Test
    public void testDescribeImage() {
        final ChatModel model = QwenChatModel.builder()
                .apiKey(Env.ALI_AI_KEY.get())
                .modelName(Model.QWEN.QWEN_VL_PLUS)
                .build();

        final UserMessage userMessage = UserMessage.from(
                TextContent.from("描述一下这张图片"),
                ImageContent.from(Image.builder().url("https://pic.rmb.bdstatic.com/bjh/bb8f413b5a1/241021/c5f406065fab31b238078065106ed894.jpeg").build())
        );

        final ChatResponse response = model.chat(userMessage);
        System.out.println(response.aiMessage().text());
    }

}
