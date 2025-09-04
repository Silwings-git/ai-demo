package cn.silwings.langchain4j.boot.std13_spring_boot_integration.declarative;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {

    @SystemMessage("You are a polite assistant")
    String chat(String message);

}
