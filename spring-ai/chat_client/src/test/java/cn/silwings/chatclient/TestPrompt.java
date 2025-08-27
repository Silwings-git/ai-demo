package cn.silwings.chatclient;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
public class TestPrompt {

    @Test
    public void testSystemPrompt(@Autowired ChatClient.Builder builder) {
        final ChatClient client = builder
                .defaultSystem("""
                        ```markdown
                        # IT助手提示词
                        
                        ## 定位
                        IT助手是一个专注于技术领域的人工智能助手，旨在为用户提供高效、准确的IT相关解决方案与信息支持。
                        
                        ## 能力
                        - 解决常见的IT问题（如系统配置、软件使用、网络设置等）
                        - 提供技术文档、操作指南与故障排除建议
                        - 通俗易懂地解释复杂的技术概念
                        - 支持多平台与设备的使用指导（Windows、Mac、Linux、iOS、Android等）
                        - 识别用户需求并提供建议（如推荐合适的技术工具或资源）
                        
                        ## 知识储备
                        - 熟悉操作系统、网络协议、数据库、编程语言、云服务等IT基础知识
                        - 了解最新技术趋势与工具（如AI、区块链、物联网等）
                        - 熟练掌握常见IT问题的处理方法和最佳实践
                        - 拥有广泛的IT行业术语与技术文档理解能力
                        - 支持多种编程语言（如Python、Java、JavaScript、C++等）的基本语法与应用场景
                        
                        ## 交互原则
                        - 保持专业性与亲和力之间的平衡
                        - 语言简洁明了，避免使用过于专业的术语
                        - 在不确定时，优先提供安全、保守的建议
                        - 鼓励用户提问，引导其明确具体需求
                        - 遵循“提问-分析-解答”的逻辑流程进行内容生成
                        
                        ## 礼貌
                        你需要礼貌的问候用户,当前用户:
                        -  姓名: {name}
                        -  年龄: {age}
                        ```""")
                .build();
        final String content = client.prompt()
                // 会话级别
//                .system("")
                .system(e -> e.param("name", "Silwings").param("age", "18"))
                .user("你好")
                .call()
                .content();
        System.out.println(content);
    }

    @Test
    public void testSystemPrompt(@Autowired ChatClient.Builder builder, @Value("classpath:files/prompt.md") Resource resource) {
        final ChatClient client = builder
                .defaultSystem(resource)
                .build();
        final String content = client.prompt()
                .user("你好")
                .call()
                .content();
        System.out.println(content);
    }

}
