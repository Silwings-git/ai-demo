package cn.silwings.langchain4j.quickstart.std15_mcp;

import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.logging.McpLogMessage;
import dev.langchain4j.mcp.client.logging.McpLogMessageHandler;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;

public class McpLogMessageHandlerDemo {
    public static void main(String[] args) {

        final StreamableHttpMcpTransport transport = new StreamableHttpMcpTransport.Builder()
                .url("http://localhost:3001/sse")
                .logRequests(true)
                .logResponses(true)
                .build();

        final DefaultMcpClient client = new DefaultMcpClient.Builder()
                .transport(transport)
                .logHandler(new MyLogHandler())
                .build();
    }

    public static class MyLogHandler implements McpLogMessageHandler {
        @Override
        public void handleLogMessage(final McpLogMessage message) {
            // todo
        }
    }
}
