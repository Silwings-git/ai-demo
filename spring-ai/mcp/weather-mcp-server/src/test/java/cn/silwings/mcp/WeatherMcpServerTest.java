package cn.silwings.mcp;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class WeatherMcpServerTest {

    @Test
    public void testLink() {
        final HttpClientSseClientTransport transport = HttpClientSseClientTransport.builder("http://localhost:8080")
                .sseEndpoint("/sse")
                .build();

        final McpSyncClient mcpClient = McpClient.sync(transport).build();

        mcpClient.initialize();

        McpSchema.ListToolsResult toolsList = mcpClient.listTools();
        System.out.println(toolsList.tools());

        McpSchema.CallToolResult weather = mcpClient.callTool(
                new McpSchema.CallToolRequest("getWeather",
                        Map.of("cityName", "上海")));
        System.out.println(weather.content());

        mcpClient.closeGracefully();
    }

}
