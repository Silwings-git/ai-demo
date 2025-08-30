package cn.silwings.langchain4j.quickstart.std02_chat_memory;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.List;
import java.util.Map;

import static org.mapdb.Serializer.STRING;

/**
 * {@link <a href="https://docs.langchain4j.info/tutorials/chat-memory">聊天记忆</a>}
 */
public class ServiceWithPersistentMemoryDemo {

    interface Assistant {
        String chat(String message);
    }

    public static void main(String[] args) {
        final MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(new PersistentChatMemoryStore())
                .build();

        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .build();

        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();

//        final String answer = assistant.chat("Hello! My name is Silwings.");
//        System.out.println(answer);

        // 运行上面的内容后,注释掉上面,取消注释下面,重新运行

        String answerWithName = assistant.chat("What is my name?");
        System.out.println(answerWithName);
    }

    // You can create your own implementation of ChatMemoryStore and store chat memory whenever you'd like
    static class PersistentChatMemoryStore implements ChatMemoryStore {

        private final DB db = DBMaker.fileDB("chat-memory.db").transactionEnable().make();
        private final Map<String, String> map = db.hashMap("messages", STRING, STRING).createOrOpen();

        @Override
        public List<ChatMessage> getMessages(final Object memoryId) {
            final String json = this.map.get((String) memoryId);
            return ChatMessageDeserializer.messagesFromJson(json);
        }

        @Override
        public void updateMessages(final Object memoryId, final List<ChatMessage> messages) {
            final String json = ChatMessageSerializer.messagesToJson(messages);
            this.map.put((String) memoryId, json);
            this.db.commit();
        }

        @Override
        public void deleteMessages(final Object memoryId) {
            this.map.remove((String) memoryId);
            this.db.commit();
        }
    }

}
