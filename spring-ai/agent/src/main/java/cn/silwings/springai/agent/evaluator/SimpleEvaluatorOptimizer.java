package cn.silwings.springai.agent.evaluator;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleEvaluatorOptimizer {

    private final ChatClient chatClient;

    private static final String GENERATOR_PROMPT = """
            你是一个Java代码生成助手，请根据任务要求生成高质量的Java代码。
            重要提醒：
            - 第一次生成时，创建一个基础但完整的实现
            - 如果收到反馈，请仔细分析每一条建议并逐一改进
            - 每次迭代都要在前一版本基础上显著提升代码质量
            - 不要一次性实现所有功能，而是逐步完善
            
            必须以JSON格式回复：
            {"thoughts":"详细说明本轮的改进思路","response":"改进后的Java代码"}""";

    // 中文评估器提示词
    private static final String EVALUATOR_PROMPT = """
            你是一个非常严格的面试官。请从以下维度严格评估代码：
            1. 代码是否高效：从底层分析每一个类型以满足最佳性能！
            
            评估标准：
            - 只有当代码满足要求达到优秀水平时才返回PASS
            - 如果任何一个维度有改进空间，必须返回NEEDS_IMPROVEMENT
            - 提供具体、详细的改进建议
            
            必须以JSON格式回复：
            {"evaluation":"PASS或NEEDS_IMPROVEMENT或FAIL","feedback":"Some suggestions"}
            
            记住：宁可严格也不要放松标准！
            """;

    int iteration = 0;
    String context = "";

    public RefinedResponse loop(final String task) {
        System.out.printf("第%s次迭代%n", iteration);

        // 生成代码
        final Generation generation = this.generate(task, context);

        // 评估代码
        final EvaluationResponse evaluation = this.evaluate(generation.response(), task);
        System.out.println(evaluation);

        if (EvaluationResponse.Evaluation.PASS.equals(evaluation.evaluation())) {
            System.out.println("代码通过评估!");
            return new RefinedResponse(generation.response());
        } else {
            // 准备下一轮的上下文
            context = """
                    之前的尝试:
                    %s
                    评估反馈:
                    %s
                    
                    请根据评估反馈重新生成代码""".formatted(generation.response(), evaluation.feedback());
            iteration++;
            return loop(task);
        }

    }

    private EvaluationResponse evaluate(final String response, final String task) {
        return this.chatClient.prompt()
                .system(EVALUATOR_PROMPT)
                .user("任务要求: %s%n生成的代码: %s".formatted(task, response))
                .advisors(SimpleLoggerAdvisor.builder().build())
                .call()
                .entity(EvaluationResponse.class);
    }

    private Generation generate(final String task, final String context) {
        return this.chatClient.prompt()
                .system(GENERATOR_PROMPT)
                .user(context.isEmpty() ? task : "%s%n%s".formatted(task, context))
                .advisors(SimpleLoggerAdvisor.builder().build())
                .call()
                .entity(Generation.class);
    }

    public record EvaluationResponse(String response, Evaluation evaluation, String feedback) {

        public enum Evaluation {
            PASS, NEEDS_IMPROVEMENT, FAIL
        }
    }

    public record Generation(String response, String thoughts) {
    }

    public record RefinedResponse(String response) {
    }
}
