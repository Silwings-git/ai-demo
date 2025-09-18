package cn.silwings.rag.ELT;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Objects;

@SpringBootTest
public class MarkdownReaderTest {

    @Test
    public void testReaderMD(@Value("classpath:rag/一键启动.md") Resource resource) {
        final MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                .withHorizontalRuleCreateDocument(false)
                .withIncludeCodeBlock(false)
                .withIncludeBlockquote(false)
                .withAdditionalMetadata("filename", Objects.requireNonNull(resource.getFilename()))
                .build();

        final MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
        final List<Document> documents = reader.read();
        for (final Document document : documents) {
            System.out.println("------------------------------");
            System.out.println(document);
        }
    }

}
