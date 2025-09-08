package cn.silwings;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final DashScopeChatModel model;

    @GetMapping("/demo")
    public User demo() {

        final ChatClient chatClient = ChatClient.builder(model)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        final User content = chatClient.prompt()
                .user("提取用户信息: hello,我叫Silwings,今年18岁,请多关照!")
                .call()
                .entity(User.class);
        System.out.println("content = " + content);
        return content;
    }

}
