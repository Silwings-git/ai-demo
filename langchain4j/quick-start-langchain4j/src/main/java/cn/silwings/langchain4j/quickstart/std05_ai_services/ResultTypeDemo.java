package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.tool.ToolExecution;

import java.util.List;

public class ResultTypeDemo {

    interface Assistant {
        // 通过Result类型可以获取更多信息
        @UserMessage("Generate an outline for the article on the following topic: {{it}}")
        Result<List<String>> generateOutlineFor(String topic);
    }

    public static void main(String[] args) {

        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();

        final Assistant assistant = AiServices.create(Assistant.class, model);

        Result<List<String>> result = assistant.generateOutlineFor("Java");

        List<String> outline = result.content();
        TokenUsage tokenUsage = result.tokenUsage();
        List<Content> sources = result.sources();
        List<ToolExecution> toolExecutions = result.toolExecutions();
        FinishReason finishReason = result.finishReason();
        System.out.println();
    }
}
