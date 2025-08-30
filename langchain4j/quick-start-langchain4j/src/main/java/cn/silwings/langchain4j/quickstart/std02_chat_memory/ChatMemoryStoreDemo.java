package cn.silwings.langchain4j.quickstart.std02_chat_memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link <a href="https://docs.langchain4j.info/tutorials/chat-memory">聊天记忆</a>}
 */
public class ChatMemoryStoreDemo {

    public static void main(String[] args) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id("12345")
                .maxMessages(10)
                .chatMemoryStore(new MyMemoryChatMemoryStore())
                .build();

        final Object id = chatMemory.id();
        System.out.println(id);
        chatMemory.add(UserMessage.from("one"));
        chatMemory.add(UserMessage.from("two"));
        chatMemory.add(UserMessage.from("three"));
    }

    static class MyMemoryChatMemoryStore implements ChatMemoryStore {

        private final Map<String, String> storage;

        public MyMemoryChatMemoryStore() {
            this.storage = new ConcurrentHashMap<>();
        }

        @Override
        public List<ChatMessage> getMessages(final Object memoryId) {
            // TODO: 实现通过记忆ID从持久化存储中获取所有消息。
            // 可以使用ChatMessageDeserializer.messageFromJson(String)和
            // ChatMessageDeserializer.messagesFromJson(String)辅助方法
            // 轻松地从JSON反序列化聊天消息。
            final String json = this.storage.get(memoryId.toString());
            return ChatMessageDeserializer.messagesFromJson(json);
        }

        @Override
        public void updateMessages(final Object memoryId, final List<ChatMessage> messages) {
            // TODO: 实现通过记忆ID更新持久化存储中的所有消息。
            // 可以使用ChatMessageSerializer.messageToJson(ChatMessage)和
            // ChatMessageSerializer.messagesToJson(List<ChatMessage>)辅助方法
            // 轻松地将聊天消息序列化为JSON。
            this.storage.put(memoryId.toString(), ChatMessageSerializer.messagesToJson(messages));
        }

        @Override
        public void deleteMessages(final Object memoryId) {
            // TODO: 实现通过记忆ID删除持久化存储中的所有消息。
            this.storage.remove(memoryId.toString());
        }
    }

}
