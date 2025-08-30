package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;

public class SimpleAiServiceDemo {

    interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .build();

        final Assistant assistant = AiServices.create(Assistant.class, model);

        final String answer = assistant.chat("Hello, tell me a joke");
        System.out.println("answer = " + answer);
    }

}
