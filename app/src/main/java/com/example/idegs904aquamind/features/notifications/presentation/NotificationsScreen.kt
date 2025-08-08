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
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Icono más grande y con mejor diseño
                            Card(
                                modifier = Modifier.size(120.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                ),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Notifications,
                                        contentDescription = "Sin notificaciones",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            
                            // Texto principal
                            Text(
                                text = "¡Todo está al día!",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            // Texto secundario
                            Text(
                                text = "No tienes notificaciones pendientes.\nTe avisaremos cuando lleguen nuevas.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            
                            // Botón de prueba
                            OutlinedButton(
                                onClick = { notificationHelper.mostrarNotificacionPrueba() },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Probar Notificación")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Icono de error con mejor diseño
                        Card(
                            modifier = Modifier.size(120.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "Error",
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        
                        // Texto principal
                        Text(
                            text = "Oops, algo salió mal",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        // Texto secundario
                        Text(
                            text = "No pudimos cargar las notificaciones.\nVerifica tu conexión e intenta de nuevo.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        
                        // Botones de acción
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.cargarNotificaciones() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
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