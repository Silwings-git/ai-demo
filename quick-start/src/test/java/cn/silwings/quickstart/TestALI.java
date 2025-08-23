package cn.silwings.quickstart;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeSpeechSynthesisApi;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TestALI
 * @Description
 * @Author Silwings
 * @Date 2025/8/22 17:09
 * @Since
 **/
@SpringBootTest
public class TestALI {

    @Test
    public void testQwen(@Autowired DashScopeChatModel chatModel) {
        chatModel.stream("你是谁?")
                .doOnNext(System.out::println)
                .blockLast();
    }

    @Test
    public void text2Img(@Autowired DashScopeImageModel imageModel) throws InterruptedException {
        final DashScopeImageOptions options = DashScopeImageOptions.builder()
                .withModel("wanx2.1-t2i-turbo")
                .build();
        final ImagePrompt prompt = new ImagePrompt("一只鹅", options);
        final String taskId = imageModel.submitImageGenTask(prompt);

        DashScopeImageApi.DashScopeImageAsyncReponse res = null;

        while (null == res) {
            res = imageModel.getImageGenTask(taskId);
            if ("running".equalsIgnoreCase(res.output().taskStatus())) {
                res = null;
                TimeUnit.SECONDS.sleep(1);
            }
        }

        res.output().results().forEach(e -> System.out.println(e.url()));
    }

    @Test
    public void text2Img2(@Autowired DashScopeImageModel imageModel) {
        final DashScopeImageOptions options = DashScopeImageOptions.builder()
                .withModel("wanx2.1-t2i-turbo")
                .withN(1)
//                .withWidth()
//                .withHeight()
//                .withMaskImageUrl()
                .build();
        final ImagePrompt prompt = new ImagePrompt("一只鹅", options);
        final ImageResponse res = imageModel.call(prompt);

        final String url = res.getResult().getOutput().getUrl();

        System.out.println(url);
    }

    @Test
    public void text2Audio(@Autowired DashScopeSpeechSynthesisModel model) throws IOException {
        final DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
                .voice("longfeifei_v2")
//                .speed()
                .model("cosyvoice-v2")
                .responseFormat(DashScopeSpeechSynthesisApi.ResponseFormat.MP3)
                .build();
        final SpeechSynthesisResponse res = model.call(new SpeechSynthesisPrompt("您好,请问是终极无敌至尊欧皇殿下吗?我仰慕你好久啦!", options));

        final File file = new File(System.getProperty("user.dir") + "/output.mp3");
        try (final FileOutputStream fos = new FileOutputStream(file)) {
            final ByteBuffer buffer = res.getResult().getOutput().getAudio();
            fos.write(buffer.array());
        }
    }
}