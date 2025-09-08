package cn.silwings.chatclient.memory;

import com.alibaba.cloud.ai.memory.redis.JedisRedisChatMemoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@SpringBootTest
public class TestRedisMemory {

    private ChatClient chatClient;

    @BeforeEach
    public void init(@Autowired ChatClient.Builder builder, @Autowired ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @TestConfiguration
    public static class ChatMemoryConfig {

        @Bean
        JedisRedisChatMemoryRepository jedisRedisChatMemoryRepository() {
            return JedisRedisChatMemoryRepository.builder()
                    .host("localhost")
                    .port(6379)
                    .build();
        }

        @Bean
        ChatMemory chatMemory(JedisRedisChatMemoryRepository jedisRedisChatMemoryRepository) {
            return MessageWindowChatMemory.builder()
                    .maxMessages(10)
                    .chatMemoryRepository(jedisRedisChatMemoryRepository)
                    .build();
        }

    }

    @Test
    public void testJdbcMemory() {

        this.chatClient.prompt("hello,My name is Silwings").call().content();

        final String content = this.chatClient.prompt("What is my name?").call().content();

        log.info("content:{}", content);
    }

    @Test
    public void testMemoryEachUser(){
        final String content = this.chatClient.prompt()
                .user("My name is Silwings")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "1"))
                .call()
                .content();
        System.out.println("content = " + content);
        System.out.println("--------------------------------------------");

        final String response1 = this.chatClient.prompt()
                .user("What is my name?")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "1"))
                .call()
                .content();
        System.out.println("response1 = " + response1);
        System.out.println("--------------------------------------------");

        final String response2 = this.chatClient.prompt()
                .user("What is my name?")
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, "2"))
                .call()
                .content();
        System.out.println("response2 = " + response2);
        System.out.println("--------------------------------------------");
    }


}
