package cn.silwings.langchain4j.quickstart.std02_chat_memory;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;

/**
 * {@link <a href="https://docs.langchain4j.info/tutorials/chat-memory">聊天记忆</a>}
 */
public class ServiceWithMemoryDemo {

    interface Assistant {
        String chat(String message);
    }

    public static void main(String[] args) {

        final MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .build();

        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();

        final String answer = assistant.chat("Hello! My name is Silwings.");
        System.out.println(answer);

        final String answerWithName = assistant.chat("What is my name?");
        System.out.println(answerWithName);
    }

}
