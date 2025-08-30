package cn.silwings.langchain4j.boot;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, streamingChatModel = "ollamaStreamingChatModel")
interface OllamaStreamingAssistant {
    @SystemMessage("You are a polite assistant")
    Flux<String> chat(String userMessage);
}