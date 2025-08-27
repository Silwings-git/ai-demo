package cn.silwings.aidemo.common.utils;

public enum Env {

    // 阿里百炼 API 秘钥
    ALI_AI_KEY,

    // deepseek API 秘钥
    DEEP_SEEK_KEY,
    ;

    public String get() {
        return System.getenv(this.name());
    }

}