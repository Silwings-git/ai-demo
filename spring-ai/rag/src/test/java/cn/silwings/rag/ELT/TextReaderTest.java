package cn.silwings.rag.ELT;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class TextReaderTest {

    @Test
    public void testReaderText(@Value("classpath:rag/创约·魔法禁书目录 01.txt") Resource resource) {
        final TextReader textReader = new TextReader(resource);
        final List<Document> documents = textReader.read();
        for (final Document document : documents) {
            System.out.println("----------------------");
            System.out.println(document);
        }
    }

}
