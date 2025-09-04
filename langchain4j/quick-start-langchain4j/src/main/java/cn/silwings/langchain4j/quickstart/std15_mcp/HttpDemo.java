package cn.silwings.langchain4j.quickstart.std15_mcp;


import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;

public class HttpDemo {
    public static void main(String[] args) {
        final StreamableHttpMcpTransport build = new StreamableHttpMcpTransport.Builder()
                .url("http://localhost:3001/sse")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
