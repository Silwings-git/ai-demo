package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

public class SystemMessageDemo {

    interface Friend {
        @SystemMessage("You are a good friend of mine. Answer using slang.")
        String chat(String userMessage);
    }

    interface Friend2 {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .build();

//        final Friend friend = AiServices.create(Friend.class, model);
//        final String answer = friend.chat("Hello");
//        System.out.println("answer = " + answer);

        final Friend2 friend2 = AiServices.builder(Friend2.class)
                .chatModel(model)
                .systemMessageProvider(memoryId -> {
                    System.out.println("memoryId: " + memoryId);
                    return "You are a good friend of mine. Answer using slang.";
                })
                .build();
        final String answer2 = friend2.chat("Hello");
        System.out.println("answer2 = " + answer2);
    }

}
