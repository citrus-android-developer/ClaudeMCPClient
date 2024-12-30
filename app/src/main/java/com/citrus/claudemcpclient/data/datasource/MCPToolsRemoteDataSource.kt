package com.citrus.claudemcpclient.data.datasource

import com.citrus.claudemcpclient.core.network.MCPToolsService
import com.citrus.claudemcpclient.core.model.ToolRequest
import com.citrus.claudemcpclient.core.model.ToolResponse
import javax.inject.Inject

class MCPToolsRemoteDataSource @Inject constructor(
    private val service: MCPToolsService
) {
    suspend fun executeTool(request: ToolRequest): ToolResponse {
        return try {
            val result = service.executeTool(request.name, request.parameters)
            ToolResponse(result = result)
        } catch (e: Exception) {
            ToolResponse(result = null, error = e.message)
        }
    }

    suspend fun getAvailableTools(): List<String> {
        return service.getAvailableTools()
    }
}