package com.citrus.claudemcpclient.domain.model

sealed class ToolExecutionResult {
    object Loading : ToolExecutionResult()
    data class Success(val data: Any?) : ToolExecutionResult()
    data class Error(val message: String) : ToolExecutionResult()
}