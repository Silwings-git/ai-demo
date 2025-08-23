package cn.silwings.quickstart;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeSpeechSynthesisApi;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesis;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisParam;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;

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
        final SpeechSynthesisResponse res = model.call(new SpeechSynthesisPrompt("您好，我是java练习生!", options));

        final File file = new File(System.getProperty("user.dir") + "/output.mp3");
        try (final FileOutputStream fos = new FileOutputStream(file)) {
            final ByteBuffer buffer = res.getResult().getOutput().getAudio();
            fos.write(buffer.array());
        }
    }

//    @Test
//    public void testAudio2Text(@Autowired DashScopeAudioTranscriptionModel model) throws MalformedURLException {
//        final String AUDIO_RESOURCES_URL = "https://pic.rmb.bdstatic.com/bjh/news/422d8fb919b80493a598aa2244c66ad4.jpeg";
//        final DashScopeAudioTranscriptionOptions options = DashScopeAudioTranscriptionOptions.builder()
////                .withModel()
//                .build();
//        final AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(new UrlResource(AUDIO_RESOURCES_URL), options);
//        model.stream(prompt)
//                .mapNotNull(e -> e.getResult().getOutput())
//                .doOnNext(System.out::println)
//                .blockLast();
//    }
//
//    @Test
//    public void testAudio2Text2(@Autowired DashScopeAudioTranscriptionModel model) throws MalformedURLException {
//        final String AUDIO_RESOURCES_URL = "https://pic.rmb.bdstatic.com/bjh/news/422d8fb919b80493a598aa2244c66ad4.jpeg";
//        final DashScopeAudioTranscriptionOptions options = DashScopeAudioTranscriptionOptions.builder()

    /// /                .withModel()
//                .build();
//        final AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(new UrlResource(AUDIO_RESOURCES_URL), options);
//        final AudioTranscriptionResponse res = model.call(prompt);
//        System.out.println(res.getResult().getOutput());
//    }
    @Test
    public void testMultimodal(@Autowired DashScopeChatModel model) {
        final ClassPathResource audioFile = new ClassPathResource("/files/cat.png");

        final Media media = new Media(MimeTypeUtils.IMAGE_JPEG, audioFile);
        final DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withMultiModel(true)
                .withModel("qwen-vl-max-latest")
                .build();

        final Prompt prompt = Prompt.builder()
                .chatOptions(options)
                .messages(UserMessage.builder()
                        .media(media)
                        .text("识别图片")
                        .build())
                .build();
        model.stream(prompt)
                .mapNotNull(e -> e.getResult().getOutput().getText())
                .doOnNext(System.out::print)
                .blockLast();
    }

//    @Test
//    public void testMultimodalSpeech2Text(@Autowired DashScopeChatModel model) {
//
//        final ClassPathResource audioFile = new ClassPathResource("/files/hello.mp3");
//
//        final Media media = new Media(MimeTypeUtils.parseMimeType("audio/mp3"), audioFile);
//        final DashScopeChatOptions options = DashScopeChatOptions.builder()
//                .withMultiModel(true)
//                .withModel("qwen-omni-turbo")
//                .build();
//
//        final Prompt prompt = Prompt.builder().chatOptions(options)
//                .messages(UserMessage.builder()
//                        .media(media)
//                        .metadata(Map.of(DashScopeApiConstants.MESSAGE_FORMAT, MessageFormat.VIDEO))
//                        .text("识别语言文件")
//                        .build())
//                .build();
//        model.stream(prompt)
//                .mapNotNull(e -> e.getResult().getOutput().getText())
//                .doOnNext(System.out::print)
//                .blockLast();
//    }

    @Test
    public void testText2Video(@Autowired Environment env) throws NoApiKeyException, InputRequiredException {
        final VideoSynthesis vs = new VideoSynthesis();
        final VideoSynthesisParam param = VideoSynthesisParam.builder()
                .model("wanx2.1-t2v-turbo")
                .prompt("笔记本在飞")
                .size("1280*720")
                .apiKey(env.getProperty("spring.ai.dashscope.api-key"))
                .build();
        final VideoSynthesisResult res = vs.call(param);
        System.out.println(res.getOutput().getVideoUrl());
    }
}