package cn.silwings.langchain4j.quickstart.std04_response_streaming;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

import java.util.concurrent.CountDownLatch;

public class StreamingChatResponseHandlerDemo {

    public static void main(String[] args) throws InterruptedException {
        final OllamaStreamingChatModel model = OllamaStreamingChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .build();

        final CountDownLatch latch = new CountDownLatch(1);
        model.chat("Tell me a joke", new StreamingChatResponseHandler() {

            final StringBuilder sb = new StringBuilder();

            @Override
            public void onPartialResponse(final String partialResponse) {
                this.sb.append(partialResponse);
                System.out.println("onPartialResponse: " + partialResponse);
            }

            @Override
            public void onCompleteResponse(final ChatResponse completeResponse) {
                System.out.println("onCompleteResponse: " + completeResponse);
                System.out.println("完成时返回全部数据? " + this.sb.toString().equals(completeResponse.aiMessage().text()));
                latch.countDown();
            }

            @Override
            public void onError(final Throwable error) {
                error.printStackTrace();
                latch.countDown();
            }
        });
        latch.await();
    }

}
