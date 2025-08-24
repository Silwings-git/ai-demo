package cn.silwings.chatclient;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
public class TestChatClient {

    @Test
    public void testChatClient(final @Autowired ChatClient.Builder builder) {

        final ChatClient client = builder.build();
        final String content = client.prompt()
                .user("你好")
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void testStreamChatClient(final @Autowired ChatClient.Builder builder) {
        final ChatClient client = builder.build();
        final Flux<String> flux = client.prompt()
                .user("你好")
                .stream()
                .content();
        flux.toIterable().forEach(System.out::print);
    }

}
