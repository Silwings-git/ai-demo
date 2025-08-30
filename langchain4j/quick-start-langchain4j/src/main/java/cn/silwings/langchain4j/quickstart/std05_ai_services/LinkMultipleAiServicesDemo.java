package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Env;
import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * @link <a href="https://docs.langchain4j.info/tutorials/ai-services#%E9%93%BE%E6%8E%A5%E5%A4%9A%E4%B8%AA-ai-%E6%9C%8D%E5%8A%A1">链接多个 AI 服务</a>
 */
public class LinkMultipleAiServicesDemo {

    /**
     * AI 服务可以作为常规（确定性）软件组件使用并与之结合：
     * <p>
     * 1. 可以一个接一个地调用 AI 服务（即链接）。
     * </br>
     * 2. 可以使用确定性和 LLM 驱动的 if/else 语句（AI 服务可以返回 boolean）。
     * </br>
     * 3. 可以使用确定性和 LLM 驱动的 switch 语句（AI 服务可以返回 enum）。
     * </br>
     * 4. 可以使用确定性和 LLM 驱动的 for/while 循环（AI 服务可以返回 int 和其他数值类型）。
     * </br>
     * 5. 可以在单元测试中模拟 AI 服务（因为它是一个接口）。
     * </br>
     * 6. 可以单独对每个 AI 服务进行集成测试。
     * </br>
     * 7.可以单独评估并找到每个 AI 服务的最佳参数。
     * </br>
     * 等等
     * </p>
     * <p>
     * 考虑一个简单的例子。 我想为我的公司构建一个聊天机器人。
     * 如果用户向聊天机器人打招呼， 我希望它用预定义的问候语回应，而不依赖 LLM 生成问候语。
     * 如果用户提出问题，我希望 LLM 使用公司的内部知识库生成回应（即 RAG）.
     * </p>
     *
     */
    public static void main(String[] args) {
        final SilwingsTech tech = new SilwingsTech();
        final String greeting = tech.handle("Good morning");
        System.out.println("greeting = " + greeting);

        final String answer = tech.handle("你们提供哪些服务?");
        System.out.println("answer = " + answer);
    }

    public static class SilwingsTech {
        private final GreetingExpert greetingExpert = buildGreetingExpert();
        private final ChatBot chatBot = buildChatBot();

        public String handle(final String userMessage) {
            if (this.greetingExpert.isGreeting(userMessage)) {
                return "来自 Silwings Tech 的问候！我怎样才能让你的一天变得更好？";
            } else {
                return this.chatBot.reply(userMessage);
            }
        }
    }

    private static ChatBot buildChatBot() {
        // 使用更昂贵的模型完成更复杂的任务
        final QwenChatModel model = QwenChatModel.builder()
                .modelName(Model.QWEN.QWEN_MAX)
                .apiKey(Env.ALI_AI_KEY.get())
                .build();
        return AiServices.builder(ChatBot.class)
                .chatModel(model)
                .build();
    }

    private static GreetingExpert buildGreetingExpert() {
        // 使用更廉价但够用的模型来做简单任务
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();
        return AiServices.builder(GreetingExpert.class)
                .chatModel(model)
                .build();
    }

    interface GreetingExpert {
        @UserMessage("这是一个问候语吗? Text: {{it}}")
        boolean isGreeting(String text);
    }

    interface ChatBot {
        @SystemMessage("您是一家名为Silwings Tech的公司的礼貌聊天机器人")
        String reply(String userMessage);
    }

}
