package cn.silwings.mpam;

import lombok.Data;

@Data
public class MorePlatformAndModelOptions {

    /**
     * 平台
     */
    private String platform;

    /**
     * 模型
     */
    private String model;

    /**
     * 温度
     */
    private Double temperature;

}
