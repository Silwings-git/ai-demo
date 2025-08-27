package cn.silwings.springai.quickstart;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;

@SpringBootTest
public class TestOllama {

    @Test
    public void testOllama(@Autowired OllamaChatModel ollamaChatModel) {
        System.out.println(ollamaChatModel.call("你是谁？、no_think"));
        System.out.println(ollamaChatModel.call("你是谁？"));
    }

    @Test
    public void testMultimodality(@Autowired OllamaChatModel ollamaChatModel) {
        final ClassPathResource resource = new ClassPathResource("files/cat.png");
        final OllamaOptions options = OllamaOptions.builder()
                .model("gemma3")
                .build();
        final Media media = new Media(MimeTypeUtils.IMAGE_PNG, resource);

        final ChatResponse response = ollamaChatModel.call(
                new Prompt(
                        UserMessage.builder()
                                .media(media)
                                .text("识别图片")
                                .build()
                        , options
                )
        );

        System.out.println(response.getResult().getOutput().getText());
    }
}