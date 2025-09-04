package cn.silwings.langchain4j.quickstart.std15_mcp;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.boot.http.client.JdkHttpClientBuilder;

import java.util.List;

public class GitHubMcpDemo {

    public static void main(String[] args) throws Exception {
        final OllamaChatModel model = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();

        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("/usr/local/bin/docker", "run", "-e", "GITHUB_PERSONAL_ACCESS_TOKEN", "-i", "mcp/github-mcp-server:latest"))
                .logEvents(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        Bot bot = AiServices.builder(Bot.class)
                .chatModel(model)
                .toolProvider(toolProvider)
                .build();

        try {
            String response = bot.chat("Summarize the last 3 commits of the dict-i18n GitHub repository");
            System.out.println("RESPONSE: " + response);
        } finally {
            mcpClient.close();
        }
    }

    public interface Bot{
        String chat(String message);
    }

}
