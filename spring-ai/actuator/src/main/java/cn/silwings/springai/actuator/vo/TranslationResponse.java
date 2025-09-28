package cn.silwings.springai.actuator.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class TranslationResponse {

    private String originalText;

    private String translatedText;

}
