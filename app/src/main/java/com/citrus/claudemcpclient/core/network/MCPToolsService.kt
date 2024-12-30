package com.citrus.claudemcpclient.core.network

import io.modelcontextprotocol.kotlin.sdk.client.Client
import io.modelcontextprotocol.kotlin.sdk.client.WebSocketClientTransport
import io.modelcontextprotocol.kotlin.sdk.Implementation
import io.modelcontextprotocol.kotlin.sdk.model.Tool
import io.modelcontextprotocol.kotlin.sdk.model.ToolExecutionRequest
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
            try {
                client = Client(
                    clientInfo = Implementation(
                        name = "claude-mcp-client",
                        version = "1.0.0"
                    )
                )
                
                transport = WebSocketClientTransport(websocketUrl)
                client.connect(transport)
            } catch (e: Exception) {
                throw MCPToolsException("Failed to initialize MCP client: ${e.message}")
            }
        }
    }

    suspend fun executeTool(name: String, params: Map<String, Any>): String {
        return withContext(Dispatchers.IO) {
            try {
                if (!this@MCPToolsService::client.isInitialized) {
                    throw MCPToolsException("MCP client is not initialized")
                }

                val request = ToolExecutionRequest(
                    name = name,
                    parameters = params
                )
                
                val response = client.executeTool(request)
                response.result.toString()
            } catch (e: Exception) {
                throw MCPToolsException("Failed to execute tool: ${e.message}")
            }
        }
    }

    suspend fun getAvailableTools(): List<Tool> {
        return withContext(Dispatchers.IO) {
            try {
                if (!this@MCPToolsService::client.isInitialized) {
                    throw MCPToolsException("MCP client is not initialized")
                }
                client.listTools()
            } catch (e: Exception) {
                throw MCPToolsException("Failed to get available tools: ${e.message}")
            }
        }
    }

    fun disconnect() {
        if (this::transport.isInitialized) {
            transport.close()
        }
    }
}

class MCPToolsException(message: String) : Exception(message)