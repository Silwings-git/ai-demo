package cn.silwings.langchain4j.quickstart.std12_image_models;

import cn.silwings.aidemo.common.utils.Env;
import cn.silwings.aidemo.common.utils.Model;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.Gson;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;

public class GenerateImageModelDemo {
    public static void main(String[] args) throws NoApiKeyException {
        openAi();
//        qwen();
    }

    private static void openAi() {
        ImageModel model = OpenAiImageModel.builder()
                .apiKey(Env.ALI_AI_KEY.get())
                .modelName(Model.QWEN.WANX2_1_T2I_TURBO)
                .baseUrl("https://dashscope.aliyuncs.com/api/v1/services/aigc/text2image/image-synthesis")
                .build();

        Response<Image> response = model.generate("Donald Duck in New York, cartoon style");

        System.out.println(response.content().url());
    }

    private static void qwen() throws NoApiKeyException {
        ImageSynthesis imageSynthesis = new ImageSynthesis();
        final ImageSynthesisResult result = imageSynthesis.call(ImageSynthesisParam.builder()
                .apiKey(Env.ALI_AI_KEY.get())
                .model(Model.QWEN.WANX2_1_T2I_TURBO)
                .prompt("一只白色的猫")
                .build());

        System.out.println(new Gson().toJson(result));
    }
}
