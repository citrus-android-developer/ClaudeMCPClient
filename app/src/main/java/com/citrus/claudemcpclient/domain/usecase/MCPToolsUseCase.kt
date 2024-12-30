package com.citrus.claudemcpclient.domain.usecase

import com.citrus.claudemcpclient.core.model.ToolRequest
import com.citrus.claudemcpclient.data.repository.MCPToolsRepository
import io.modelcontextprotocol.kotlin.sdk.model.Tool
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MCPToolsUseCase @Inject constructor(
    private val repository: MCPToolsRepository
) {
    suspend fun initialize(websocketUrl: String) {
        repository.initialize(websocketUrl)
    }

    suspend fun executeTool(toolName: String, params: Map<String, Any>): Flow<Any?> {
        val request = ToolRequest(toolName, params)
        return repository.executeTool(request)
            .map { response ->
                if (response.error != null) {
                    throw Exception(response.error)
                }
                response.result
            }
    }

    suspend fun getAvailableTools(): List<Tool> {
        return repository.getAvailableTools()
            .map { tools -> tools.sortedBy { it.name } }
            .collect { it }
    }

    fun disconnect() {
        repository.disconnect()
    }
}