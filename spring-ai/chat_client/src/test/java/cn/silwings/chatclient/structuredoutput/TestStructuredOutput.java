package cn.silwings.chatclient.structuredoutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestStructuredOutput {

    private ChatClient chatClient;

    @BeforeEach
    public void init(@Autowired ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Test
    public void testBoolOut() {
        final Boolean isNegative = this.chatClient.prompt()
                .system("判断用户是否表达了负面情绪")
                .user("气死我了")
                .call()
                .entity(Boolean.class);
        if (Boolean.TRUE.equals(isNegative)) {
            System.out.println("用户不开心");
        } else {
            System.out.println("用户开心");
        }
    }

}
