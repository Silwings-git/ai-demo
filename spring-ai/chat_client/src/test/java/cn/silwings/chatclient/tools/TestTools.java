package cn.silwings.chatclient.tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTools {

    private ChatClient chatClient;

    @BeforeEach
    public void init(@Autowired ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @Test
    public void testTools(@Autowired UserTools userTools) {
        final String content = this.chatClient.prompt("查询小明的信息")
                .tools(userTools)
                .call()
                .content();
        System.out.println(content);
    }

}
