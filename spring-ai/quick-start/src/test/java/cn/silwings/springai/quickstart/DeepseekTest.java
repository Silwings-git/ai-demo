package cn.silwings.springai.quickstart;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeepseekTest {

    @Test
    public void testDeepseek(@Autowired DeepSeekChatModel deepSeekChatModel) {
        final String content = deepSeekChatModel.call("你好,你是谁?");
        System.out.println(content);
    }

    @Test
    public void testDeepseekStream(@Autowired DeepSeekChatModel deepSeekChatModel) {
        deepSeekChatModel.stream("你好,你是谁?")
                .doOnNext(System.out::print)
                .blockLast();
    }

    @Test
    public void testChatOptions(@Autowired DeepSeekChatModel deepSeekChatModel) {
        final DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                .temperature(1D)
                .maxTokens(500)
//                .stop(List.of("，"))
                .build();
        final Prompt prompt = new Prompt("请写一句诗描述清晨.", options);
        deepSeekChatModel.stream(prompt)
                .mapNotNull(e -> e.getResult().getOutput().getText())
                .doOnNext(System.out::print)
                .blockLast();
    }

    @Test
    public void testDeepseekReasoning(@Autowired DeepSeekChatModel deepSeekChatModel) {
        final DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                .model(DeepSeekApi.ChatModel.DEEPSEEK_REASONER)
                .build();
        final Prompt prompt = new Prompt("你是谁?", options);
        deepSeekChatModel.stream(prompt)
                .map(e -> (DeepSeekAssistantMessage) e.getResult().getOutput())
                .filter(e -> null != e.getReasoningContent())
                .doOnNext(e -> System.out.print(e.getReasoningContent()))
                .blockLast();

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println();

        deepSeekChatModel.stream(prompt)
                .map(e -> (DeepSeekAssistantMessage) e.getResult().getOutput())
                .filter(e -> null != e.getText())
                .doOnNext(e -> System.out.print(e.getText()))
                .blockLast();

    }

}