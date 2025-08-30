package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.memory.ChatMemoryAccess;

import java.util.List;

/**
 * @link <a href="https://docs.langchain4j.info/tutorials/ai-services#%E8%81%8A%E5%A4%A9%E8%AE%B0%E5%BF%86">聊天记忆</a>
 */
public class ChatMemoryDemo {
    public static void main(String[] args) {
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();
//        singleMemory(model);
//        memoryForEachUser(model);
        accessMemoryForEachUser(model);
    }

    private static void accessMemoryForEachUser(final OllamaChatModel model) {
        interface Assistant extends ChatMemoryAccess {
            // 请注意，如果 AI 服务方法没有用 @MemoryId 注解的参数， ChatMemoryProvider 中 memoryId 的值将默认为字符串 "default"。
            String chat(@MemoryId int memoryId, @UserMessage String message);
        }

        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                // 每个记忆id对应一个记忆
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();

        final String answerToKlaus = assistant.chat(1, "Hello, my name is Klaus");
        System.out.println("answerToKlaus = " + answerToKlaus);
        final String answerToFrancine = assistant.chat(2, "Hello, my name is Francine");
        System.out.println("answerToFrancine = " + answerToFrancine);

        final List<ChatMessage> messagesWithKlausBefore = assistant.getChatMemory(1).messages();
        System.out.println("before: " + ChatMessageSerializer.messagesToJson(messagesWithKlausBefore));
        boolean chatMemoryWithFrancineEvicted = assistant.evictChatMemory(1);
        System.out.println("chatMemoryWithFrancineEvicted = " + chatMemoryWithFrancineEvicted);
        final ChatMemory chatMemory = assistant.getChatMemory(1);
        System.out.println("after: is null -> " + (null == chatMemory));
    }

    private static void memoryForEachUser(final OllamaChatModel model) {
        interface Assistant {
            String chat(@MemoryId int memoryId, @UserMessage String message);
        }

        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                // 每个记忆id对应一个记忆
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();

        final String answerToKlaus = assistant.chat(1, "Hello, my name is Klaus");
        System.out.println("answerToKlaus = " + answerToKlaus);
        final String answerToFrancine = assistant.chat(2, "Hello, my name is Francine");
        System.out.println("answerToFrancine = " + answerToFrancine);
    }


    /**
     * 全局使用同一个记忆
     */
    private static void singleMemory(final OllamaChatModel model) {
        interface Assistant {
            String chat(String message);
        }
        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}
