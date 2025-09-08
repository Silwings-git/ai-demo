package cn.silwings;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatClientDemo {

    @Test
    public void testEntity() {

        final OllamaApi ollamaApi = OllamaApi.builder().build();

        final OllamaChatModel model = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(
                        OllamaOptions.builder()
                                .model("qwen3:14b")
                                .temperature(0.9)
                                .build())
                .build();
        final ChatClient chatClient = ChatClient.builder(model).build();
        final User content = chatClient.prompt()
                .user("提取用户信息: hello,我叫Silwings,今年18岁,请多关照!")
                .call()
                .entity(User.class);
        System.out.println("content = " + content);
    }

}
