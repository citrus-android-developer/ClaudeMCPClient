package com.citrus.claudemcpclient.core.model

data class ToolError(
    val code: String,
    val message: String,
    val cause: Throwable? = null
)