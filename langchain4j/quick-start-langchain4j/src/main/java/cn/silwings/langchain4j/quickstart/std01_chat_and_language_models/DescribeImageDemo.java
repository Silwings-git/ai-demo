package cn.silwings.langchain4j.quickstart.std01_chat_and_language_models;

import cn.silwings.aidemo.common.utils.Env;
import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;

public class DescribeImageDemo {
    public static void main(String[] args) {
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
