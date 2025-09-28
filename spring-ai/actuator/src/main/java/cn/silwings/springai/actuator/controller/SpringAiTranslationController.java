package cn.silwings.springai.actuator.controller;

import cn.silwings.springai.actuator.vo.TranslationRequest;
import cn.silwings.springai.actuator.vo.TranslationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SpringAiTranslationController {

    private final ChatClient chatClient;

    @PostMapping("/translate")
    public TranslationResponse translate(@RequestBody TranslationRequest request) {
        log.info("翻译请求: {}->{}", request.getSourceLanguage(), request.getTargetLanguage());

        final String prompt = String.format(
                "作为专业翻译助手,请将以下%s文本翻译成%s, 保持原文的语气.\r\n%s",
                request.getSourceLanguage(),
                request.getTargetLanguage(),
                request.getText()
        );

        final String translatedText = this.chatClient.prompt()
                .user(prompt)
                .advisors(SimpleLoggerAdvisor.builder().build())
                .call()
                .content();

        return TranslationResponse.builder()
                .originalText(request.getText())
                .translatedText(translatedText)
                .build();
    }
}
