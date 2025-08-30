package cn.silwings.langchain4j.quickstart.std02_chat_memory;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class ChatMemoryDemo {

    public static void main(String[] args) {

        final MessageWindowChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .build();

        // You have full control over the chat memory.
        // You can decide if you want to add a particular message to the memory
        // (e.g. you might not want to store few-shot examples to save on tokens).
        // You can process/modify the message before saving if required.

        memory.add(UserMessage.userMessage("Hello, my name is Klaus"));
        AiMessage answer = model.chat(memory.messages()).aiMessage();
        System.out.println(answer.text()); // Hello Klaus! How can I assist you today?
        memory.add(answer);

        memory.add(UserMessage.userMessage("What is my name?"));
        AiMessage answerWithName = model.chat(memory.messages()).aiMessage();
        System.out.println(answerWithName.text()); // Your name is Klaus.
        memory.add(answerWithName);
    }

}
