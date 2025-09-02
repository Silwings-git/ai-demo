package cn.silwings.langchain4j.quickstart.std10_classification;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

public class SentimentClassification {

    private static final OllamaChatModel chatModel = OllamaChatModel.builder()
            .modelName(Model.Ollama.QWEN_3_14B)
            .baseUrl("http://localhost:11434")
            .think(false)
            .logRequests(true)
            .logResponses(true)
            .build();

    // 情感枚举
    enum Sentiment {
        // 积极
        POSITIVE,
        // 中性
        NEUTRAL,
        // 消极
        NEGATIVE
    }

    // AI 驱动的情感分析器接口
    interface SentimentAnalyzer {

        @UserMessage("分析情感: {{it}}")
        Sentiment analyzeSentimentOf(String text);

        @UserMessage("有积极情绪吗? {{it}}")
        boolean isPositive(String text);
    }

    public static void main(String[] args) {
        final SentimentAnalyzer sentimentAnalyzer = AiServices.create(SentimentAnalyzer.class, chatModel);

        final Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf("我喜欢这台笔记本");
        // 预期输出 POSITIVE
        System.out.println(sentiment);

        boolean positive = sentimentAnalyzer.isPositive("真是糟糕的一天");
        // 预期输出: false
        System.out.println(positive);
    }

}
