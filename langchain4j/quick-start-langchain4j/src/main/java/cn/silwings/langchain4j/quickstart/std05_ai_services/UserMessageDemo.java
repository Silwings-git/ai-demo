package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import lombok.Data;
import lombok.experimental.Accessors;

public class UserMessageDemo {

    interface Friend {
        @UserMessage("Hello, My name is {{hello.msg}}")
        String chat(@V("hello") Hello hello);
    }

    @Data
    @Accessors(chain = true)
    public static class Hello{
        private String msg;
    }

    public static void main(String[] args) {
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .build();
        final Friend friend = AiServices.create(Friend.class, model);
        final String answer = friend.chat(new Hello().setMsg("Silwings"));
        System.out.println("answer = " + answer);
    }

}
