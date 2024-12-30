package com.citrus.claudemcpclient.presentation.mcp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.modelcontextprotocol.kotlin.sdk.Tool
import kotlinx.serialization.json.Json

@Composable
fun MCPToolsScreen(
    modifier: Modifier = Modifier,
    viewModel: MCPToolsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val availableTools by viewModel.availableTools.collectAsState()
    var selectedTool by remember { mutableStateOf<Tool?>(null) }
    var parameters by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        DropdownMenu(
            expanded = false,
            onDismissRequest = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            availableTools.forEach { tool ->
                DropdownMenuItem(
                    text = { Text(tool.name) },
                    onClick = { selectedTool = tool }
                )
            }
        }

        selectedTool?.let { tool ->
            Text(
                text = tool.description ?: "No description available",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        OutlinedTextField(
            value = parameters,
            onValueChange = { parameters = it },
            label = { Text("Parameters (JSON format)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                selectedTool?.let { tool ->
                    try {
                        val paramMap = Json.decodeFromString<Map<String, Any>>(parameters)
                        viewModel.executeTool(tool.name, paramMap)
                    } catch (e: Exception) {
                        // Handle JSON parsing error
                    }
                }
            },
            enabled = selectedTool != null,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Execute Tool")
        }

        when (uiState) {
            is MCPToolsState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is MCPToolsState.Success -> {
                ToolExecutionCard(
                    toolName = selectedTool?.name ?: "",
                    result = (uiState as MCPToolsState.Success).data.toString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is MCPToolsState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                            text = (uiState as MCPToolsState.Error).message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            MCPToolsState.Initial -> {
                Text(
                    text = "Select a tool and provide parameters to begin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}