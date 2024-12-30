package com.citrus.claudemcpclient.core.model

import io.modelcontextprotocol.kotlin.sdk.model.Tool

data class ToolResponse(
    val result: Any?,
    val error: String? = null,
    val tool: Tool? = null
)