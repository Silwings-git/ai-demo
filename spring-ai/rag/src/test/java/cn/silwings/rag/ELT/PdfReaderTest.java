package cn.silwings.rag.ELT;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest
public class PdfReaderTest {

    @Test
    public void testReaderPdf(@Value("classpath:rag/1795.pdf") Resource resource) {
        // 按页读取
        final PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
        final List<Document> documents = reader.read();
        for (final Document document : documents) {
            System.out.println("------------------------------");
            System.out.println(document);
        }
    }

//    @Test
//    public void testReaderPdf2(@Value("classpath:rag/1796.pdf") Resource resource) {
//        // 按目录读取
//        final ParagraphPdfDocumentReader reader = new ParagraphPdfDocumentReader(resource);
//        final List<Document> documents = reader.read();
//        for (final Document document : documents) {
//            System.out.println("------------------------------");
//            System.out.println(document);
//        }
//    }
}
