package cn.silwings.langchain4j.quickstart.std05_ai_services;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

/**
 * @link <a href="https://docs.langchain4j.info/tutorials/ai-services#%E6%B5%81%E5%BC%8F%E5%A4%84%E7%90%86">流式处理</a>
 */
public class StreamDemo {

    public static void main(String[] args) throws InterruptedException {
        final OllamaStreamingChatModel model = OllamaStreamingChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();
        tokenStream(model);
        fluxStream(model);
    }

    private static void fluxStream(final OllamaStreamingChatModel model) {
        interface Assistant {
            Flux<String> chat(String message);
        }

        final Assistant assistant = AiServices.create(Assistant.class, model);

        final Flux<String> flux = assistant.chat("Tell me a joke");
        System.out.println("fluxStream");
        flux.doOnNext(System.out::println).blockLast();
    }

    private static void tokenStream(final OllamaStreamingChatModel model) throws InterruptedException {
        interface Assistant {
            TokenStream chat(String message);
        }

        final Assistant assistant = AiServices.create(Assistant.class, model);

        final TokenStream tokenStream = assistant.chat("Tell me a joke");

        System.out.println("tokenStream");
        final CountDownLatch latch = new CountDownLatch(1);
        tokenStream.onPartialResponse(System.out::println)
                .onRetrieved(System.out::println)
                .onToolExecuted(System.out::println)
                .onCompleteResponse(chatResponse -> {
                    System.out.println(chatResponse);
                    latch.countDown();
                })
                .onError(Throwable::printStackTrace)
                .start();
        latch.await();
    }

}
