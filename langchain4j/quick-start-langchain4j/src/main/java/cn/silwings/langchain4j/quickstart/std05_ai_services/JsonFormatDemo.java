package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.ollama.OllamaChatModel;

/**
 * @link <a href="https://docs.langchain4j.info/tutorials/ai-services#json-%E6%A8%A1%E5%BC%8F">JSON 模式</a>
 */
public class JsonFormatDemo {
    public static void main(String[] args) {
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .logResponses(true)
                //  开启json模式,不同模型的api不同,但关键字有json,response
                .responseFormat(ResponseFormat.JSON)
                .build();

        final String hello = model.chat("hello");
        System.out.println(hello);
    }

}
