package cn.silwings.langchain4j.quickstart.std09_structured_outputs;

import cn.silwings.aidemo.common.utils.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

public class HighLevelJsonSchemaDemo {

    interface PersonExtractor {
        @SystemMessage("你是一个专门从文本中提取人物信息的助手。")
        Person extractPersonFrom(String text);
    }


    public static void main(String[] args) throws JsonProcessingException {

        final String text = """
                John is 42 years old and lives an independent life.
                He stands 1.75 meters tall and carries himself with confidence.
                Currently unmarried, he enjoys the freedom to focus on his personal goals and interests.
                """;

        final OllamaChatModel chatModel = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .responseFormat(ResponseFormat.JSON)
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();

        final PersonExtractor personExtractor = AiServices.builder(PersonExtractor.class)
                .chatModel(chatModel)
                .build();

        final Person person = personExtractor.extractPersonFrom(text);
        System.out.println(person);
    }

    @Description("一个人")
    record Person(@JsonProperty(required = true) @Description("人的名和姓，例如：John Doe") String name,
                  @Description("人的年龄，例如：42") int age,
                  @Description("人的身高（米），例如：1.78") double height,
                  @Description("人是否已婚，例如：false") boolean married) {
    }
}
