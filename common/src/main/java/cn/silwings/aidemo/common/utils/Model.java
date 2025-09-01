package cn.silwings.aidemo.common.utils;

public final class Model {

    public static final class QWEN {
        // 通义千问-Max
        public static final String QWEN_MAX = "qwen-max";
        // 通亿千问 turbo
        public static final String QWEN_TURBO = "qwen-turbo";
        // 通义千问VL-Plus
        public static final String QWEN_VL_PLUS = "qwen-vl-plus";
        // 通义万相2.1-文生图-Turbo
        public static final String WANX2_1_T2I_TURBO = "wanx2.1-t2i-turbo";
        // 语音生成cosyvoice-v2大模型
        public static final String COSYVOICE_V2 = "cosyvoice-v2";
    }

    public static final class DeepSeek {
        // 聊天模型
        public static final String DEEPSEEK_CHAT = "deepseek-chat";
        // 思考模型
        public static final String DEEPSEEK_REASONER = "deepseek-reasoner";
        public static final String DEEP_SEEK_V_3_1 = "deepseek-v3.1";
        public static final String DEEP_SEEK_V_3 = "deepseek-v3";
    }

    public static final class Ollama {
        public static final String QWEN_3_0_6B = "qwen3:0.6b";
        public static final String QWEN_3_14B = "qwen3:14b";
    }
}