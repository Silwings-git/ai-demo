package cn.silwings.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class EmbeddingTest {

    @Test
    public void testEmbedding(@Autowired DashScopeEmbeddingModel model) {
        final float[] embedded = model.embed("我是Silwings");
        System.out.println("embedded.length = " + embedded.length);
        System.out.println(Arrays.toString(embedded));
    }

}
