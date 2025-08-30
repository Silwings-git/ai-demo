package cn.silwings.langchain4j.chat.translation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.service.output.OutputParsingException;
import io.github.haibiiin.json.repair.JSONRepair;
import io.github.haibiiin.json.repair.UnableHandleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItTranslateAgent {

    private final TranslationExpert expert;

    private final TranslateCorrection correction;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String translate(final int userId, final String content) {
        String text = null;
        try {
            int maxTryCount = 3;
            text = this.expert.translate(userId, content).content();

            for (int i = 0; i < maxTryCount; i++) {
                final JudgingResults judging = this.judging(userId, content, text);
                if (judging.getScore() > 95) {
                    return text;
                }
                if (i < maxTryCount - 1) {
                    text = this.expert.repair(userId, judging.getSuggestion()).content();
                }
            }
            return text;
        } catch (Exception e) {
            log.error("翻译失败: {}", e.getMessage(), e);
            return text;
        } finally {
            this.expert.evictChatMemory(userId);
            this.correction.evictChatMemory(userId);
        }
    }

    private JudgingResults judging(final int userId, final String content, final String text) {
        try {
            return this.correction.judging(userId, content, text).content();
        } catch (OutputParsingException e) {
            try {
                return this.objectMapper.readValue(this.handleJsonParseError(e.getMessage()), JudgingResults.class);
            } catch (JsonProcessingException ex) {
                throw e;
            }
        }
    }

    /**
     * 处理错误信息，提取并修复其中的JSON
     *
     * @param errorMessage 包含JSON解析错误的信息
     * @return 修复后的JSON字符串，若不符合格式则返回null
     */
    public String handleJsonParseError(String errorMessage) {
        // 检查字符串是否符合指定格式
        if (errorMessage == null || !errorMessage.startsWith("Failed to parse \"")) {
            return null;
        }

        // 使用正则表达式提取JSON部分
        // 匹配"Failed to parse \""开头，中间是JSON内容，然后包含" into "的模式
        final Pattern pattern = Pattern.compile("^Failed to parse \"(.*?)\" into .*$", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(errorMessage);

        if (matcher.find()) {
            String jsonText = matcher.group(1);
            System.out.println("jsonText = " + jsonText);
            final JSONRepair repair = new JSONRepair();
            try {
                return repair.handle(jsonText);
            } catch (UnableHandleException e) {
                if (!jsonText.equals("\"")) {
                    return repair.handle(jsonText + "\"");
                } else {
                    throw e;
                }
            }
        }

        return null;
    }
}
