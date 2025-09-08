package cn.silwings.chatclient;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestAdvisor {

    @Test
    public void testLogger(final @Autowired ChatClient.Builder builder) {

        final ChatClient client = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();

        final String content = client.prompt()
                .user("你好")
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void testSafeGuard(final @Autowired ChatClient.Builder builder) {

        final ChatClient client = builder
                .defaultAdvisors(new SimpleLoggerAdvisor(), SafeGuardAdvisor.builder().sensitiveWords(List.of("Silwings")).build())
                .build();

        final String content = client.prompt()
                .user("你好,Silwings")
                .call()
                .content();
        System.out.println(content); // I'm unable to respond to that due to sensitive content. Could we rephrase or discuss something else?
    }

}
