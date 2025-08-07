package com.example.idegs904aquamind.features.controllers.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.features.controllers.presentation.components.NodoCard

@Composable
fun ControllersScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = androidx.compose.runtime.remember { ControllersViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()
    val nodosWithLoading by viewModel.nodosWithLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNodos()
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is ControllersUiState.Loading -> {
                LoadingScreen()
            }
            is ControllersUiState.Success -> {
                ControllersContent(
                    nodosWithLoading = nodosWithLoading,
                    onRefresh = { viewModel.actualizarInmediatamente() },
                    onToggleClick = { nodo -> viewModel.toggleNodoEstatus(nodo) }
                )
            }
            is ControllersUiState.Error -> {
                ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.loadNodos() }
                )
            }
        }
    }
}

@Composable
private fun ControllersContent(
    nodosWithLoading: List<NodoWithLoadingState>,
    onRefresh: () -> Unit,
    onToggleClick: (Nodo) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            ControllersHeader(nodosWithLoading = nodosWithLoading, onRefresh = onRefresh)
        }
        
        items(nodosWithLoading) { nodoWithLoading ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                NodoCard(
                    nodo = nodoWithLoading.nodo,
                    onToggleClick = { onToggleClick(nodoWithLoading.nodo) },
                    isLoading = nodoWithLoading.isLoading,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun ControllersHeader(
    nodosWithLoading: List<NodoWithLoadingState>,
    onRefresh: () -> Unit
) {
    val activeCount = nodosWithLoading.count { it.nodo.id_estatus == 1 }
    val totalCount = nodosWithLoading.size
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1976D2),
                        Color(0xFF1565C0)
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Tune,
                    contentDescription = "Controles",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Control de Dispositivos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Indicador de actualización automática
                    Icon(
                        imageVector = Icons.Filled.Sync,
                        contentDescription = "Actualización automática activa",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                    
                    IconButton(
                        onClick = onRefresh,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Actualizar ahora",
                            tint = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatCard(
                    title = "Activos",
                    value = activeCount.toString(),
                    total = totalCount.toString(),
                    icon = Icons.Filled.Power,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    title = "Inactivos", 
                    value = (totalCount - activeCount).toString(),
                    total = totalCount.toString(),
                    icon = Icons.Filled.PowerOff,
                    color = Color(0xFFF44336),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    total: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(end = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando dispositivos...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = "Error",
                tint = Color(0xFFF44336),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Error",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reintentar",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reintentar")
            }
        }
    }
} 