package cn.silwings.langchain4j.quickstart.std07_tools;

import cn.silwings.aidemo.common.utils.Model;
import com.google.gson.Gson;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.util.List;
import java.util.Map;

public class LowLevelToolSpecificationDemo {

    public static void main(String[] args) {

        // 作为开发者，我们应该使用LLM提供的参数执行工具，并将工具执行的结果反馈给LLM。
        // 将工具执行的结果发送回 LLM时, 需要创建一个 ToolExecutionResultMessage（每个 ToolExecutionRequest 对应一个） 并将其与所有先前的消息一起发送：

//        createdManually();

        assistedMethodCreation();
    }

    private static void assistedMethodCreation() {
        final List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(WeatherTools.class);

        final MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        final ChatRequest chatRequest = ChatRequest.builder()
                .messages(UserMessage.from("明天上海的天气怎么样?"))
                .toolSpecifications(toolSpecifications)
                .build();

        chatMemory.add(chatRequest.messages());

        final OllamaChatModel model = getOllamaChatModel();

        final AiMessage aiMessage = model.chat(chatRequest).aiMessage();
        chatMemory.add(aiMessage);

        if (aiMessage.hasToolExecutionRequests()) {
            for (final ToolExecutionRequest toolExecutionRequest : aiMessage.toolExecutionRequests()) {
                final String toolExecutionResult = callTool(toolExecutionRequest.name(), toolExecutionRequest.arguments());
                if (null != toolExecutionResult) {
                    final ToolExecutionResultMessage message = ToolExecutionResultMessage.from(toolExecutionRequest, toolExecutionResult);
                    chatMemory.add(message);
                }
            }
        } else {
            System.out.println("没有call tool");
            return;
        }

        final ChatRequest feedbackToolExecuteResultRequest = ChatRequest.builder()
                .messages(chatMemory.messages())
                .toolSpecifications(toolSpecifications)
                .build();

        final AiMessage aiMessage2 = model.chat(feedbackToolExecuteResultRequest).aiMessage();

        System.out.println(ChatMessageSerializer.messageToJson(aiMessage2));
    }

    @SuppressWarnings("unchecked")
    private static String callTool(final String name, final String arguments) {
        if ("getWeather".equals(name)) {
            final Map<String, String> map = new Gson().fromJson(arguments, Map.class);
            return new WeatherTools().getWeather(map.get("city"), TemperatureUnit.valueOf(map.get("temperatureUnit")));
        }
        return null;
    }

    private static OllamaChatModel getOllamaChatModel() {
        return OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    public static class WeatherTools {
        @Tool("返回给定城市的天气预报")
        public String getWeather(@P("应返回天气预报的城市") String city, final TemperatureUnit temperatureUnit) {
            return "36摄氏度";
        }
    }

    public enum TemperatureUnit {
        CELSIUS, FAHRENHEIT
    }

    private static void createdManually() {
        final ToolSpecification specification = ToolSpecification.builder()
                .name("getWeather")
                .description("返回给定城市的天气预报")
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("city", "应返回天气预报的城市")
                        .addEnumProperty("temperatureUnit", List.of("CELSIUS", "FAHRENHEIT"))
                        .required("city")
                        .build())
                .build();

        final ChatRequest chatRequest = ChatRequest.builder()
                .messages(UserMessage.from("明天上海的天气怎么样?"))
                .toolSpecifications(specification)
                .build();

        final OllamaChatModel model = getOllamaChatModel();

        final AiMessage aiMessage = model.chat(chatRequest).aiMessage();
        System.out.println(ChatMessageSerializer.messageToJson(aiMessage));
    }

}
