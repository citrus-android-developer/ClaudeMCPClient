package com.citrus.claudemcpclient.data.repository

import com.citrus.claudemcpclient.data.datasource.MCPToolsRemoteDataSource
import com.citrus.claudemcpclient.core.model.ToolRequest
import com.citrus.claudemcpclient.core.model.ToolResponse
import io.modelcontextprotocol.kotlin.sdk.model.Tool
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MCPToolsRepository @Inject constructor(
    private val remoteDataSource: MCPToolsRemoteDataSource
) {
    suspend fun initialize(websocketUrl: String) {
        remoteDataSource.initialize(websocketUrl)
    }

    suspend fun executeTool(request: ToolRequest): Flow<ToolResponse> = flow {
        emit(ToolResponse(result = null))
        try {
            val response = remoteDataSource.executeTool(request)
            emit(response)
        } catch (e: Exception) {
            emit(ToolResponse(result = null, error = e.message))
        }
    }

    suspend fun getAvailableTools(): Flow<List<Tool>> = flow {
        try {
            val tools = remoteDataSource.getAvailableTools()
            emit(tools)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    fun disconnect() {
        remoteDataSource.disconnect()
    }
}