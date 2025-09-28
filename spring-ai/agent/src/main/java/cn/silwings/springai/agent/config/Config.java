package cn.silwings.springai.agent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class Config {

    @Bean
    public ChatClient chatClient(final ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    @Scope("prototype")
    RestClient.Builder restClientBuilder(final RestClientBuilderConfigurer configurer) {
        final RestClient.Builder builder = RestClient.builder()
                .requestFactory(ClientHttpRequestFactoryBuilder.simple()
                        .withCustomizer(c -> {
                            c.setReadTimeout(Duration.ofSeconds(60));
                            c.setConnectTimeout(Duration.ofSeconds(60));
                        })
                        .build());
        return configurer.configure(builder);
    }
}
