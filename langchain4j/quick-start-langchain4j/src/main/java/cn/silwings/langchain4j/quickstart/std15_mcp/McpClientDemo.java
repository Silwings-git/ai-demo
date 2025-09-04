package cn.silwings.langchain4j.quickstart.std15_mcp;

import cn.silwings.aidemo.common.utils.Model;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;


import java.util.List;

public class McpClientDemo {
    public static void main(String[] args) {

        final StreamableHttpMcpTransport transport = new StreamableHttpMcpTransport.Builder()
                .url("http://localhost:3001/sse")
                .logRequests(true)
                .logResponses(true)
                .build();

        final DefaultMcpClient client = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        final McpToolProvider provider = McpToolProvider.builder()
                .mcpClients(List.of(client))
                .build();

        final OllamaChatModel chatModel = OllamaChatModel.builder()
                .modelName(Model.Ollama.QWEN_3_14B)
                .baseUrl("http://localhost:11434")
                .responseFormat(ResponseFormat.JSON)
                .think(false)
                .logRequests(true)
                .logResponses(true)
                .build();

        final Bot bot = AiServices.builder(Bot.class)
                .chatModel(chatModel)
                .toolProvider(provider)
                .build();
    }
    public interface Bot{
        String chat(String message);
    }
}
