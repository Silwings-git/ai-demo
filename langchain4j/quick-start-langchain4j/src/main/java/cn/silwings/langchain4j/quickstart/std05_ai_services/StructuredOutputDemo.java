package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Model;
import com.google.gson.Gson;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import lombok.Data;

import java.time.LocalDate;

/**
 * @link <a href="https://docs.langchain4j.info/tutorials/ai-services#%E7%BB%93%E6%9E%84%E5%8C%96%E8%BE%93%E5%87%BA">结构化输出</a>
 */
public class StructuredOutputDemo {

    public static void main(String[] args) throws InterruptedException {
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();
        sentimentAnalyzer(model);
        priorityAnalyzer(model);
        personExtractor(model);
    }

    @Data
    static class Person {
        @Description("first name of a person") // 可以添加可选描述，帮助 LLM 更好地理解
        String firstName;
        String lastName;
        LocalDate birthDate;
        Address address;
    }

    @Data
    @Description("an address") // 可以添加可选描述，帮助 LLM 更好地理解
    static class Address {
        String street;
        Integer streetNumber;
        String city;
    }

    /**
     * 使用pojo类型作为结果
     */
    private static void personExtractor(final OllamaChatModel model) {
        interface PersonExtractor {
            @UserMessage("Extract information about a person from {{it}}")
            Person extractPersonFrom(String text);
        }

        final PersonExtractor personExtractor = AiServices.create(PersonExtractor.class, model);

        final String text = """
                In 1968, amidst the fading echoes of Independence Day,
                a child named John arrived under the calm evening sky.
                This newborn, bearing the surname Doe, marked the start of a new journey.
                He was welcomed into the world at 345 Whispering Pines Avenue
                a quaint street nestled in the heart of Springfield
                an abode that echoed with the gentle hum of suburban dreams and aspirations.
                """;

        final Person person = personExtractor.extractPersonFrom(text);

        System.out.println(new Gson().toJson(person));
    }

    /**
     * 使用枚举类型作为结果
     */
    private static void priorityAnalyzer(final OllamaChatModel model) {
        enum Priority {
            CRITICAL, HIGH, LOW
        }

        interface PriorityAnalyzer {
            //            @UserMessage("Analyze the priority of the following issue: {{it}}")
            @UserMessage("分析一下问题的优先级: {{it}}")
            Priority analyzePriority(String issueDescription);
        }

        final PriorityAnalyzer priorityAnalyzer = AiServices.create(PriorityAnalyzer.class, model);

        final String issueDescription = "The main payment gateway is down, and customers cannot process transactions.";
        final Priority priority = priorityAnalyzer.analyzePriority(issueDescription);

        System.out.println(priority);
    }

    /**
     * 使用boolean类型的结果
     */
    private static void sentimentAnalyzer(final OllamaChatModel model) {

        interface SentimentAnalyzer {
            @UserMessage("Does {{it}} has a positive sentiment?")
            boolean isPositive(String text);
        }

        final SentimentAnalyzer analyzer = AiServices.create(SentimentAnalyzer.class, model);

        boolean positive = analyzer.isPositive("It's wonderful!");

        System.out.println("positive = " + positive);
    }

}
