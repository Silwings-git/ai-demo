package cn.silwings.chatclient.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class UserTools {

    @Tool(description = "获取用户信息")
    public User getUser(@ToolParam(description = "用户名称") String username) {
        return new User(1, username, 18);
    }

    public record User(int id, String username, Integer age) {
    }

}
