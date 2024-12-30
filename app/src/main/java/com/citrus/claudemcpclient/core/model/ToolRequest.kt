package com.citrus.claudemcpclient.core.model

data class ToolRequest(
    val name: String,
    val parameters: Map<String, Any>
)