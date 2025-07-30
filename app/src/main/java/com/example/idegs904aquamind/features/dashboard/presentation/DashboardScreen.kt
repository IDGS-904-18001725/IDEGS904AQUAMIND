package com.example.idegs904aquamind.features.dashboard.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.TipoPeriodo
import com.example.idegs904aquamind.features.graficos.presentation.components.ConsumoChart

@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember { DashboardViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarDashboard()
    }

    Column(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is DashboardUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is DashboardUiState.Success -> {
                val nombreUsuario = (uiState as DashboardUiState.Success).nombreUsuario
                val eventos = (uiState as DashboardUiState.Success).eventos
                
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Mensaje de bienvenida
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Bienvenido",
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Bienvenido, $nombreUsuario",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Aquí tienes un resumen de tu consumo de agua",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                    
                    // Gráfica de consumo
                    if (eventos.isNotEmpty()) {
                        ConsumoChart(
                            eventos = eventos,
                            tipoPeriodo = TipoPeriodo.DIAS,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.WaterDrop,
                                    contentDescription = "Sin datos",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "No hay datos de consumo disponibles",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Los datos de consumo aparecerán aquí cuando estén disponibles",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
            
            is DashboardUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Error",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Error al cargar el dashboard",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = (uiState as DashboardUiState.Error).message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.cargarDashboard() }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            
            is DashboardUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}