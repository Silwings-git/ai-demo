package cn.silwings.langchain4j.quickstart.std15_mcp;

import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;

import java.util.List;

public class StdioDemo {

    public static void main(String[] args) {
        final StdioMcpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("/usr/bin/npm", "exec", "@modelcontextprotocol/server-everything@0.6.2"))
                .logEvents(true)
                .build();
    }

}
