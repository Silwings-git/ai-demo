package cn.silwings.langchain4j.quickstart.std02_chat_memory;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;

/**
 * {@link <a href="https://docs.langchain4j.info/tutorials/chat-memory">聊天记忆</a>}
 */
public class ServiceWithMemoryForEachUserExample {

    interface Assistant {
        String chat(@MemoryId int memoryId, @dev.langchain4j.service.UserMessage String userMessage);
    }

    public static void main(String[] args) {
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .build();

        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();

        System.out.println("A: " + assistant.chat(1, "Hello, my name is Silwings"));

        System.out.println("B: " + assistant.chat(2, "Hello, my name is Jack"));

        System.out.println("A: " + assistant.chat(1, "What is my name?"));

        System.out.println("B: " + assistant.chat(2, "What is my name?"));
    }

}
