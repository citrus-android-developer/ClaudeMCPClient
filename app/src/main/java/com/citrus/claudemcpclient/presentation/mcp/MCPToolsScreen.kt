package com.citrus.claudemcpclient.presentation.mcp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.modelcontextprotocol.kotlin.sdk.model.Tool

@Composable
fun MCPToolsScreen(
    modifier: Modifier = Modifier,
    viewModel: MCPToolsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showToolSelector by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Tool Selection
        OutlinedButton(
            onClick = { showToolSelector = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(uiState.selectedTool?.name ?: "Select a Tool")
        }

        if (showToolSelector) {
            AlertDialog(
                onDismissRequest = { showToolSelector = false },
                title = { Text("Select Tool") },
                text = {
                    Column {
                        uiState.availableTools.forEach { tool ->
                            TextButton(
                                onClick = {
                                    viewModel.selectTool(tool)
                                    showToolSelector = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(tool.name)
                            }
                        }
                    }
                },
                confirmButton = {}
            )
        }

        // Selected Tool Description
        uiState.selectedTool?.let { tool ->
            Text(
                text = tool.description ?: "No description available",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Parameters Input
        OutlinedTextField(
            value = uiState.parameters,
            onValueChange = { viewModel.updateParameters(it) },
            label = { Text("Parameters (JSON format)") },
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Execute Button
        Button(
            onClick = { viewModel.executeTool() },
            enabled = uiState.selectedTool != null && !uiState.isLoading,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Execute Tool")
        }

        // Result/Error Display
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    ErrorCard(
                        error = uiState.error!!,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                uiState.result != null -> {
                    ResultCard(
                        toolName = uiState.selectedTool?.name ?: "",
                        result = uiState.result.toString(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ErrorCard(error: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun ResultCard(
    toolName: String,
    result: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tool: $toolName",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Result:",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = result,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}