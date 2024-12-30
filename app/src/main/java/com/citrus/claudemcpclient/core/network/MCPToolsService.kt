package com.citrus.claudemcpclient.core.network

import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.WebSocketClientTransport
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.Tool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MCPToolsService @Inject constructor() {
    private lateinit var client: Client
    private lateinit var transport: WebSocketClientTransport

    suspend fun initialize(websocketUrl: String) {
        withContext(Dispatchers.IO) {
            client = Client(
                clientInfo = Implementation(
                    name = "claude-mcp-client",
                    version = "1.0.0"
                )
            )
            
            transport = WebSocketClientTransport(websocketUrl)
            client.connect(transport)
        }
    }

    suspend fun executeTool(name: String, params: Map<String, Any>): String {
        return withContext(Dispatchers.IO) {
            val request = ToolExecutionRequest(
                name = name,
                parameters = params
            )
            
            val response = client.executeTool(request)
            response.result.toString()
        }
    }

    suspend fun getAvailableTools(): List<Tool> {
        return withContext(Dispatchers.IO) {
            client.listTools()
        }
    }

    fun disconnect() {
        transport.close()
    }
}