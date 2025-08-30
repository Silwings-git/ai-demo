package cn.silwings.langchain4j.chat.translation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TranslationController {

    private final ItTranslateAgent itTranslateAgent;

    @PostMapping("/translate")
    public String translate(@RequestBody String content) {
        return this.itTranslateAgent.translate(1, content);
    }

}
