package cn.silwings.springai.actuator.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TranslationRequest {

    private String sourceLanguage;

    private String targetLanguage;

    private String text;

}
