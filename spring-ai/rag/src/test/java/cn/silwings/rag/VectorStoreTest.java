package cn.silwings.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootTest
public class VectorStoreTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public VectorStore vectorStore(@Autowired DashScopeEmbeddingModel model) {
            return SimpleVectorStore.builder(model).build();
        }
    }

    @Test
    public void testVectorStore(@Autowired VectorStore vectorStore) {
        final Document document1 = Document.builder()
                .text("大家好，我是小明，一名拥有 5 年实战经验的 Java 开发工程师。从大学时期接触 Java 语言开始，我就被它的跨平台特性和强大的生态体系深深吸引，毕业后便坚定地走上了 Java 开发这条路。")
                .build();
        final Document document2 = Document.builder()
                .text("我始终秉持 “代码即产品” 的理念，注重代码的可读性、可维护性与健壮性，坚持编写单元测试保障代码质量。同时，我喜欢主动学习新技术、新框架，经常关注 Java 社区的动态，闲暇时会通过技术博客、开源项目源码研读来补充知识储备。在团队协作中，我乐于分享技术经验，也善于倾听他人的想法，相信高效的协作能让项目事半功倍。")
                .build();
        final Document document3 = Document.builder()
                .text("我习惯每天按时吃三餐")
                .build();
        vectorStore.add(List.of(document1, document2, document3));

        final List<Document> documentList = vectorStore.similaritySearch("开发");
        for (final Document document : documentList) {
            System.out.println(document);
        }

        System.out.println("-----------------------------");

        final List<Document> documentList2 = vectorStore.similaritySearch(SearchRequest.builder()
                .query("开发")
                .topK(2)
                // 中文不宜设置太高
                .similarityThreshold(0.5)
                .build());
        for (final Document document : documentList2) {
            System.out.println(document);
        }
    }

}
