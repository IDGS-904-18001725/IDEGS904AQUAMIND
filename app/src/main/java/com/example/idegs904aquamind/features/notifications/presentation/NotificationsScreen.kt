package com.example.idegs904aquamind.features.notifications.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.features.notifications.presentation.components.NotificacionCard
import com.example.idegs904aquamind.features.notifications.presentation.components.NotificacionModal
import com.example.idegs904aquamind.features.notifications.presentation.components.NotificationStatusCard
import com.example.idegs904aquamind.features.notifications.service.NotificationHelper

@Composable
fun NotificationsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember { NotificationsViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()
    
    var notificacionSeleccionada by remember { mutableStateOf<Notificacion?>(null) }
    var mostrarModal by remember { mutableStateOf(false) }
    
    val notificationHelper = remember { NotificationHelper(context) }

    LaunchedEffect(Unit) {
        viewModel.cargarNotificaciones()
    }

    Column(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is NotificacionesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is NotificacionesUiState.Success -> {
                val notificaciones = (uiState as NotificacionesUiState.Success).notificaciones
                
                if (notificaciones.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Sin notificaciones",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "No hay notificaciones",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Cuando recibas notificaciones, aparecerán aquí",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Card de estado del sistema de notificaciones push
                        item {
                            NotificationStatusCard(
                                context = context,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                        
                        // Lista de notificaciones
                        items(notificaciones) { notificacion ->
                            NotificacionCard(
                                notificacion = notificacion,
                                onNotificacionClick = { 
                                    notificacionSeleccionada = notificacion
                                    mostrarModal = true
                                    viewModel.marcarComoLeida(notificacion)
                                }
                            )
                        }
                    }
                }
            }
            
            is NotificacionesUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Error",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Error al cargar notificaciones",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = (uiState as NotificacionesUiState.Error).message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.cargarNotificaciones() }
                            ) {
                                Text("Reintentar")
                            }
                            
                            OutlinedButton(
                                onClick = { notificationHelper.mostrarNotificacionPrueba() }
                            ) {
                                Text("Probar Notificación")
                            }
                        }
                    }
                }
            }
            
            is NotificacionesUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    // Modal de notificación
    if (mostrarModal && notificacionSeleccionada != null) {
        NotificacionModal(
            notificacion = notificacionSeleccionada!!,
            onDismiss = {
                mostrarModal = false
                notificacionSeleccionada = null
            }
        )
    }
} 