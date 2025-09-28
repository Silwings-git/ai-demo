package cn.silwings.rag;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FactCheckingTest {

    @Test
    public void testFactChecking(@Autowired DashScopeChatModel chatModel) {

        final FactCheckingEvaluator factCheckingEvaluator = new FactCheckingEvaluator(ChatClient.builder(chatModel));

        final Document doc = Document.builder()
                .text("""
                        取消预定:
                        - 最晚在航班起飞前 48 小时取消.
                        - 取消费用: 经济舱 75 美元, 商务舱 25美元""")
                .build();

        final List<Document> docs = List.of(doc);

        // 假设是AI的回答
        final String response = "经济舱取消费用是25美元";

        final EvaluationRequest evaluationRequest = new EvaluationRequest(docs, response);

        final EvaluationResponse evaluate = factCheckingEvaluator.evaluate(evaluationRequest);

        System.out.println("evaluate = " + evaluate);
    }

}
