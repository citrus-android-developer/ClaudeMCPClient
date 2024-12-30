package com.citrus.claudemcpclient.domain.usecase

import com.citrus.claudemcpclient.core.model.ToolRequest
import com.citrus.claudemcpclient.data.repository.MCPToolsRepository
import com.citrus.claudemcpclient.domain.model.ToolExecutionResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MCPToolsUseCase @Inject constructor(
    private val repository: MCPToolsRepository
) {
    suspend fun executeTool(toolName: String, params: Map<String, Any>): Flow<ToolExecutionResult> {
        val request = ToolRequest(toolName, params)
        return repository.executeTool(request)
    }

    suspend fun getAvailableTools(): Flow<List<String>> {
        return repository.getAvailableTools()
    }
}