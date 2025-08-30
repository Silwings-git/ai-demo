package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;

public class ToolsDemo {

    interface Assistant {
        String chat(String message);
    }

    static class Tools {
        @Tool
        int add(int a, int b) {
            System.out.printf("add: a = %s, b = %s%n", a, b);
            return a + b;
        }

        @Tool
        int multiply(int a, int b) {
            System.out.printf("multiply: a = %s, b = %s%n", a, b);
            return a * b;
        }
    }

    public static void main(String[] args) {
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();


        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .tools(new Tools())
                .build();

        final String answer = assistant.chat("What is 1+2 and 3*4?");

        System.out.println(answer);
    }

}
