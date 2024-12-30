package com.citrus.claudemcpclient.presentation.mcp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citrus.claudemcpclient.domain.usecase.MCPToolsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.modelcontextprotocol.kotlin.sdk.model.Tool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MCPToolsViewModel @Inject constructor(
    private val toolsUseCase: MCPToolsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MCPToolsUiState())
    val uiState: StateFlow<MCPToolsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            initializeMCP()
            loadAvailableTools()
        }
    }

    private suspend fun initializeMCP() {
        try {
            // 此處為您的 WebSocket URL
            toolsUseCase.initialize("ws://your-mcp-server-url")
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    private suspend fun loadAvailableTools() {
        try {
            _uiState.update { it.copy(isLoading = true) }
            val tools = toolsUseCase.getAvailableTools()
            _uiState.update { it.copy(
                availableTools = tools,
                isLoading = false
            ) }
        } catch (e: Exception) {
            _uiState.update { it.copy(
                error = e.message,
                isLoading = false
            ) }
        }
    }

    fun selectTool(tool: Tool) {
        _uiState.update { it.copy(selectedTool = tool) }
    }

    fun updateParameters(parameters: String) {
        _uiState.update { it.copy(parameters = parameters) }
    }

    fun executeTool() {
        val currentState = _uiState.value
        val selectedTool = currentState.selectedTool ?: return
        val parameters = currentState.parameters

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                val paramMap = Json.decodeFromString<Map<String, Any>>(parameters)
                val result = toolsUseCase.executeTool(selectedTool.name, paramMap)
                _uiState.update { it.copy(
                    result = result,
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = e.message,
                    isLoading = false
                ) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        toolsUseCase.disconnect()
    }
}