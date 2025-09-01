package cn.silwings.langchain4j.quickstart.std07_tools;

import cn.silwings.aidemo.common.utils.Model;
import com.google.gson.Gson;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProviderResult;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

public class HighLevelToolSpecificationDemo {

    interface Assistant {
        String chat(@dev.langchain4j.service.UserMessage String userMessage);
    }

    public static void main(String[] args) {
//        useToolObject();
//        programmaticallySpecifyTheTool();
//        toolProvider();
        toolIllusionStrategy();
    }

    private static void toolIllusionStrategy() {
        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(getOllamaChatModel())
                .tools(new UserTools())
                // LLM可能会出现幻觉,请求使用一个不存在的工具,这种情况下langchain4j默认会抛出一个异常报告错误
                // 可以通过为LLM服务提供在这种情况下使用的策略来配置不同的行为。
                // 这里通过向LLM返回一个响应,推送它尝试使用其他工具.
                .hallucinatedToolNameStrategy(toolExecutionRequest -> ToolExecutionResultMessage.from(
                        toolExecutionRequest, "错误：没有名为 " + toolExecutionRequest.name() + " 的工具"))
                .build();
        final String response = assistant.chat("给我王五的用户信息");
        System.out.println("response = " + response);
    }

    @SuppressWarnings("unchecked")
    private static void toolProvider() {
        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(getOllamaChatModel())
                .tools(new UserTools())
                .toolProvider(req -> {
                    if (req.userMessage().singleText().contains("爱好")) {
                        ToolSpecification toolSpecification = ToolSpecification.builder()
                                .name("getUserHobbies")
                                .description("返回用户兴趣爱好")
                                .parameters(JsonObjectSchema.builder()
                                        .addStringProperty("userCode", "用户编码")
                                        .required("userCode")
                                        .build())
                                .build();
                        return ToolProviderResult.builder()
                                .add(toolSpecification, (toolExecutionRequest, memoryId) -> {
                                    if (toolExecutionRequest.name().equals("getUserHobbies")) {
                                        final Gson gson = new Gson();
                                        final String arguments = toolExecutionRequest.arguments();
                                        final Map<String, String> map = gson.fromJson(arguments, Map.class);
                                        final String hobbies = new UserTools().getUserHobbies(map.get("userCode"));
                                        return null == hobbies ? "" : hobbies;
                                    } else {
                                        return "错误";
                                    }
                                })
                                .build();
                    } else {
                        return null;
                    }
                })
                .build();
        final String response = assistant.chat("张三有什么爱好?");
        System.out.println("response = " + response);
    }

    @SuppressWarnings("unchecked")
    private static void programmaticallySpecifyTheTool() {
        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(getOllamaChatModel())
                .tools(Map.of(
                        ToolSpecification.builder()
                                .name("searchUserCodes")
                                .description("使用给定的关键字查询相关的用户编码")
                                .parameters(JsonObjectSchema.builder()
                                        .addStringProperty("keyword", "关键字搜索")
                                        .build())
                                .build(),
                        (toolExecutionRequest, memoryId) -> {
                            final Gson gson = new Gson();
                            final String arguments = toolExecutionRequest.arguments();
                            final Map<String, String> map = gson.fromJson(arguments, Map.class);
                            return gson.toJson(new UserTools().searchUserCodes(map.get("keyword")));
                        },
                        ToolSpecification.builder()
                                .name("getUser")
                                .description("给定用户编码,返回该用户的信息")
                                .parameters(JsonObjectSchema.builder()
                                        .addStringProperty("userCode", "用户编码")
                                        .required("userCode")
                                        .build())
                                .build(),
                        (toolExecutionRequest, memoryId) -> {
                            final Gson gson = new Gson();
                            final String arguments = toolExecutionRequest.arguments();
                            final Map<String, String> map = gson.fromJson(arguments, Map.class);
                            final User user = new UserTools().getUser(map.get("userCode"));
                            if (null == user) {
                                return "";
                            }
                            return gson.toJson(user);
                        }
                ))
                .build();
        final String response = assistant.chat("给我李四的用户信息");
        System.out.println("response = " + response);
    }

    private static void useToolObject() {
        final Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(getOllamaChatModel())
                .tools(new UserTools())
                .build();
        final String response = assistant.chat("给我王五的用户信息");
        System.out.println("response = " + response);
    }

    public static class UserTools {

        private final List<User> users = List.of(
                new User().setUserCode("UC001").setUserName("张三").setAge(18),
                new User().setUserCode("UC002").setUserName("李四").setAge(27),
                new User().setUserCode("UC003").setUserName("王五").setAge(36)
        );

        private final Map<String, String> hobbies = Map.of(
                "UC001", "打乒乓球",
                "UC002", "游泳",
                "UC003", "编程"
        );

        @Tool("使用给定的关键字查询相关的用户编码")
        public List<String> searchUserCodes(@P(value = "关键字搜索", required = false) String keyword) {
            return this.users
                    .stream()
                    .filter(e -> null == keyword || e.userName.contains(keyword))
                    .map(User::getUserCode)
                    .toList();
        }

        @Tool("给定用户编码,返回该用户的信息")
        public User getUser(@P("用户编码") String userCode) {
            return this.users.stream()
                    .filter(e -> e.getUserCode().equals(userCode))
                    .findFirst().orElse(null);
        }

        public String getUserHobbies(final String userCode) {
            return this.hobbies.get(userCode);
        }
    }

    @Data
    @Accessors(chain = true)
    public static class User {
        private String userCode;
        private String userName;
        private int age;
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
}
