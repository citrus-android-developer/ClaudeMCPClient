package com.citrus.claudemcpclient.data.repository

import com.citrus.claudemcpclient.data.datasource.MCPToolsRemoteDataSource
import com.citrus.claudemcpclient.core.model.ToolRequest
import com.citrus.claudemcpclient.core.model.ToolResponse
import com.citrus.claudemcpclient.domain.model.ToolExecutionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MCPToolsRepository @Inject constructor(
    private val remoteDataSource: MCPToolsRemoteDataSource
) {
    suspend fun executeTool(request: ToolRequest): Flow<ToolExecutionResult> = flow {
        emit(ToolExecutionResult.Loading)
        try {
            val response = remoteDataSource.executeTool(request)
            if (response.error != null) {
                emit(ToolExecutionResult.Error(response.error))
            } else {
                emit(ToolExecutionResult.Success(response.result))
            }
        } catch (e: Exception) {
            emit(ToolExecutionResult.Error(e.message ?: "Unknown error"))
        }
    }

    suspend fun getAvailableTools(): Flow<List<String>> = flow {
        emit(remoteDataSource.getAvailableTools())
    }
}