package cn.silwings.langchain4j.boot.std13_spring_boot_integration.declarative;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AssistantController {

    private final Assistant assistant;

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return this.assistant.chat(message);
    }
}
