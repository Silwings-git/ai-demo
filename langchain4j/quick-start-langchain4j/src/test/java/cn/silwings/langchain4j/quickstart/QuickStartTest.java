package cn.silwings.langchain4j.quickstart;

import cn.silwings.aidemo.common.utils.Env;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;
import org.junit.jupiter.api.Test;

import static cn.silwings.aidemo.common.utils.Model.QWEN;

public class QuickStartTest {

    @Test
    public void testChat() {
        final ChatModel model = QwenChatModel.builder()
                .apiKey(Env.ALI_AI_KEY.get())
                .modelName(QWEN.QWEN_MAX)
                .build();

        final String answer = model.chat("Say 'Hello World'");
        System.out.println(answer);
    }

}