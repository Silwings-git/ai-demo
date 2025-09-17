package cn.silwings.mcp.server.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class UserToolService {

    private final Map<String, UserInfo> userInfoMap;

    public UserToolService() {
        this.userInfoMap = new ConcurrentHashMap<>();
        this.userInfoMap.put("张三", new UserInfo("张三", 10));
        this.userInfoMap.put("李四", new UserInfo("李四", 20));
        this.userInfoMap.put("王五", new UserInfo("王五", 30));
    }

    @Tool(description = "获取用户信息.包括姓名,年龄等")
    public UserInfo getUserInfo(@ToolParam(description = "用户名") String username) {
        log.info("正在获取用户信息.用户名: {}", username);
        if (this.userInfoMap.containsKey(username)) {
            return this.userInfoMap.get(username);
        }
        throw new RuntimeException("未找到用户信息,请确认用户姓名.");
    }

    public record UserInfo(String username, int age) {
    }

}
