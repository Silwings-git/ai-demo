package cn.silwings.mcp.server.sse;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ToolCallbackProvider userInfoTools(final UserToolService userToolService) {
        return MethodToolCallbackProvider.builder().toolObjects(userToolService).build();
    }

}
