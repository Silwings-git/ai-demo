package cn.silwings.chatclient.memory;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootTest
public class TestMemory {

    private ChatClient chatClient;

    @BeforeEach
    public void init(@Autowired ChatClient.Builder builder, @Autowired ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Test
    public void testMemory(@Autowired DashScopeChatModel chatModel) {

        String id = "1";

        final MessageWindowChatMemory memory = MessageWindowChatMemory.builder().build();

        final UserMessage userMessage1 = new UserMessage("hello,My name is Silwings");
        memory.add(id, userMessage1);
        final ChatResponse response1 = chatModel.call(new Prompt(memory.get(id)));
        memory.add(id, response1.getResult().getOutput());

        final UserMessage userMessage2 = new UserMessage("What is my name?");
        memory.add(id, userMessage2);
        final ChatResponse response2 = chatModel.call(new Prompt(memory.get(id)));
        memory.add(id, userMessage2);

        log.info("response2:{}", response2.getResult().getOutput());
    }

    @Test
    public void testMemory2(@Autowired ChatClient.Builder builder, @Autowired ChatMemory chatMemory) {

        final ChatClient client = builder
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();

        client.prompt("hello,My name is Silwings").call().content();

        final String content = client.prompt("What is my name?").call().content();

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
