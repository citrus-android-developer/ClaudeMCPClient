package com.citrus.claudemcpclient.presentation.mcp

import io.modelcontextprotocol.kotlin.sdk.model.Tool

sealed interface MCPToolsState {
    data object Initial : MCPToolsState
    data object Loading : MCPToolsState
    data class Success(val data: Any?) : MCPToolsState
    data class Error(val message: String) : MCPToolsState
}

data class MCPToolsUiState(
    val availableTools: List<Tool> = emptyList(),
    val selectedTool: Tool? = null,
    val parameters: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val result: Any? = null
)