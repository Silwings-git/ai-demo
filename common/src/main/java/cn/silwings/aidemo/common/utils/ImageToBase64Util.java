package cn.silwings.aidemo.common.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * 从classpath读取图片并转换为Base64编码的工具类
 */
public class ImageToBase64Util {

    /**
     * 从classpath读取图片并转换为Base64编码
     *
     * @param classpath 图片在classpath中的路径（例如："files/fish.jpg"）
     * @return Base64编码字符串（包含data URI前缀，如"image/jpeg;base64,xxx..."）
     * @throws IOException 当图片读取失败时抛出
     */
    public static String convert(final String classpath) throws IOException {
        // 1. 通过ClassPathResource获取资源输入流
        try (InputStream inputStream = new ClassPathResource(classpath).getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // 2. 读取图片字节到输出流
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 3. 获取图片MIME类型（根据实际图片类型调整）
            String mimeType = getMimeType(classpath);

            // 4. 转换为Base64并拼接data URI格式
            byte[] imageBytes = outputStream.toByteArray();
            String base64Encoded = Base64.getEncoder().encodeToString(imageBytes);
            return String.format("data:%s;base64,%s", mimeType, base64Encoded);
        }
    }

    /**
     * 根据文件路径获取MIME类型
     *
     * @param filePath 图片路径
     * @return MIME类型字符串
     */
    private static String getMimeType(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".gif")) {
            return "image/gif";
        } else if (filePath.endsWith(".bmp")) {
            return "image/bmp";
        } else {
            throw new IllegalArgumentException("不支持的图片格式: " + filePath);
        }
    }
}
