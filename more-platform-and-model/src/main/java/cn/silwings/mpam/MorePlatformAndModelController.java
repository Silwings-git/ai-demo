package cn.silwings.mpam;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class MorePlatformAndModelController {

    private final Map<String, ChatModel> models;

    public MorePlatformAndModelController(final Map<String, ChatModel> models) {
        this.models = new HashMap<>();
        models.forEach((s, model) -> this.models.put(s.replace("ChatModel", "").toLowerCase(), model));
    }

    @RequestMapping("/chat")
    public Flux<String> chat(final String message, final MorePlatformAndModelOptions options) {

        final ChatModel chatModel = this.getChatModel(options.getPlatform()).orElseThrow();

        final ChatClient.Builder builder = ChatClient.builder(chatModel);

        final ChatClient client = builder.defaultOptions(
                ChatOptions.builder()
                        .temperature(options.getTemperature())
                        .model(options.getModel())
                        .build()
        ).build();

        return client.prompt()
                .user(message)
                .stream()
                .content();
    }

    private Optional<ChatModel> getChatModel(final String platform) {
        return Optional.ofNullable(this.models.get(platform.toLowerCase()));
    }

}
