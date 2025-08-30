package cn.silwings.langchain4j.quickstart.std04_response_streaming;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.model.LambdaStreamingResponseHandler;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

import java.util.concurrent.TimeUnit;

public class LambdaStreamingResponseHandlerDemo {

    public static void main(String[] args) throws InterruptedException {
        final OllamaStreamingChatModel model = OllamaStreamingChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .build();

        // model.chat("Tell me a joke", LambdaStreamingResponseHandler.onPartialResponse(System.out::print));
        model.chat("Tell me a joke", LambdaStreamingResponseHandler.onPartialResponseAndError(System.out::print, Throwable::printStackTrace));
        TimeUnit.SECONDS.sleep(10);
    }

}
