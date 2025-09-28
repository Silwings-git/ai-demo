package cn.silwings.springai.agent;

import cn.silwings.springai.agent.evaluator.SimpleEvaluatorOptimizer;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(final DashScopeChatModel chatModel) {
        final ChatClient chatClient = ChatClient.create(chatModel);
        return args -> {
            final SimpleEvaluatorOptimizer.RefinedResponse response = new SimpleEvaluatorOptimizer(chatClient).loop("""
                    <user input>
                    面试被问: 怎么高效的将100行List<User>数据转换为Map<Id,User>,不能使用stream.
                    </user input>""");
            System.out.println("response = " + response);
        };
    }

}