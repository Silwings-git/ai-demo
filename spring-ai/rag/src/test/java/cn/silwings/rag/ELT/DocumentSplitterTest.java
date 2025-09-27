package cn.silwings.rag.ELT;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class DocumentSplitterTest {

    @Test
    public void TokenTextSplitter(@Value("classpath:rag/创约·魔法禁书目录 01.txt") Resource resource) {
        final TextReader textReader = new TextReader(resource);
        final List<Document> documents = textReader.read();

        final TokenTextSplitter splitter = new TokenTextSplitter(100, 10, 5, Integer.MAX_VALUE, true);
        final List<Document> apply = splitter.apply(documents);

        System.out.println(documents.size());
        System.out.println(apply.size());
        System.out.println("----------------");

        apply.forEach(System.out::println);
    }

    @Test
    public void ChineseTokenTextSplitter(@Value("classpath:rag/创约·魔法禁书目录 01.txt") Resource resource) {
        final TextReader textReader = new TextReader(resource);
        final List<Document> documents = textReader.read();

        final ChineseTokenTextSplitter splitter = new ChineseTokenTextSplitter(100, 10, 5, Integer.MAX_VALUE, true);
        final List<Document> apply = splitter.apply(documents);

        System.out.println(documents.size());
        System.out.println(apply.size());
        System.out.println("----------------");

        apply.forEach(System.out::println);
    }

    @Test
    public void testKeywordMetadataEnricher(@Autowired DashScopeChatModel chatModel, @Value("classpath:rag/创约·魔法禁书目录 01.txt") Resource resource) {
        final TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("filename", resource.getFilename());
        final List<Document> documents = textReader.read();

        final ChineseTokenTextSplitter splitter = new ChineseTokenTextSplitter();
        List<Document> apply = splitter.apply(documents);

        // 对文档进行关键字提取,并设置到元数据中
        final KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(chatModel, 5);
        apply = enricher.apply(apply);

        for (final Document document : apply) {
            System.out.println(document.getText());
            System.out.println(document.getText().length());
        }

        apply.forEach(System.out::println);
    }

    @Test
    public void testKeywordMetadataEnricher2(
            @Autowired VectorStore vectorStore,
            @Autowired DashScopeChatModel chatModel,
            @Value("classpath:rag/创约·魔法禁书目录 01.txt") Resource resource) {
        final TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("filename", resource.getFilename());
        List<Document> documents = textReader.read();

        final ChineseTokenTextSplitter splitter = new ChineseTokenTextSplitter();
        documents = splitter.apply(documents);

        final KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(chatModel, 5);
        documents = enricher.apply(documents);

//        for (final Document document : documents) {
//            System.out.println(document);
//        }

        vectorStore.add(documents);

        documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        // 过滤元数据
//                        .filterExpression("filename=='创约·魔法禁书目录 01.txt'")
                        .filterExpression(new FilterExpressionBuilder().in("excerpt_keywords", "御坂美琴").build())
                        .build()
        );

        System.out.println("documents.size(): " + documents.size());
        documents.forEach(System.out::println);
    }

    @Test
    public void testSummaryMetadataEnricher(
            @Autowired VectorStore vectorStore,
            @Autowired DashScopeChatModel chatModel,
            @Value("classpath:rag/创约·魔法禁书目录 01.txt") Resource resource) {

        // 读取文档
        final TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("filename", resource.getFilename());
        List<Document> documents = textReader.read();

        // 分割文档
        final ChineseTokenTextSplitter splitter = new ChineseTokenTextSplitter();
        documents = splitter.apply(documents);

        // 关键字提取
        final KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(chatModel, 5);
        documents = enricher.apply(documents);

        // 摘要提取
        final SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(chatModel,
                List.of(
                        // 上一篇
                        SummaryMetadataEnricher.SummaryType.PREVIOUS,
                        // 当前篇
                        SummaryMetadataEnricher.SummaryType.CURRENT,
                        // 下一篇
                        SummaryMetadataEnricher.SummaryType.NEXT
                ));

        documents = summaryMetadataEnricher.apply(documents);

        vectorStore.add(documents);

        documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .filterExpression(new FilterExpressionBuilder().in("excerpt_keywords", "魔神").build())
                        .build()
        );

        System.out.println("documents.size(): " + documents.size());
        documents.forEach(System.out::println);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public VectorStore vectorStore(final DashScopeEmbeddingModel embeddingModel) {
            return SimpleVectorStore.builder(embeddingModel).build();
        }

    }

}
