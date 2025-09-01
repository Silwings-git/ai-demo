package cn.silwings.langchain4j.chat.translation;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Description("评审结果")
@Accessors(chain = true)
public class JudgingResults {

    // 评审分数
    @Description("分数")
    private int score;

    // 评审建议
    @Description("建议")
    private String suggestion;

}
