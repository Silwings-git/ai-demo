package cn.silwings.langchain4j.chat.translation;

import cn.silwings.aidemo.common.utils.Env;
import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TranslationConfig {

    @Bean
    public OpenAiChatModel qwenTurbo() {
        return OpenAiChatModel.builder()
                .modelName(Model.QWEN.QWEN_TURBO)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(Env.ALI_AI_KEY.get())
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public OpenAiChatModel deepseekV3() {
        return OpenAiChatModel.builder()
                .modelName(Model.DeepSeek.DEEP_SEEK_V_3_1)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(Env.ALI_AI_KEY.get())
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(10);
    }

    @Bean
    public TranslationExpert translationExpert(final OpenAiChatModel qwenTurbo, final ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(TranslationExpert.class)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .chatModel(qwenTurbo)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }

    @Bean
    public TranslateCorrection translateCorrection(final OpenAiChatModel deepseekV3, final ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(TranslateCorrection.class)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .chatModel(deepseekV3)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}
