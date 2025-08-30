package cn.silwings.langchain4j.boot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class AssistantController {

    private final OpenAiAssistant openAiAssistant;

    private final OllamaAssistant ollamaAssistant;

    private final OllamaStreamingAssistant ollamaStreamingAssistant;

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return this.openAiAssistant.chat(message);
    }

    @GetMapping("/chat/{assistant}")
    public String chat(@RequestParam("message") String message, @PathVariable("assistant") String assistant) {
        return switch (assistant.toLowerCase()) {
            case "openai" -> this.openAiAssistant.chat(message);
            case "ollama" -> this.ollamaAssistant.chat(message);
            default -> throw new IllegalStateException("Unexpected value: " + assistant.toLowerCase());
        };
    }

    @GetMapping(value = "/chat-stream")
    public Flux<String> chatStream(@RequestParam("message") String message) {
        return this.ollamaStreamingAssistant.chat(message);
    }

}
