package com.example.idegs904aquamind.features.notifications.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.features.notifications.service.NotificationScheduler
import kotlinx.coroutines.delay

@Composable
fun NotificationStatusCard(
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    val notificationScheduler = remember { NotificationScheduler(context) }
    var estadoVerificaciones by remember { mutableStateOf("") }
    var estanActivas by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            try {
                estadoVerificaciones = notificationScheduler.obtenerEstadoVerificaciones()
                estanActivas = notificationScheduler.estanVerificacionesActivas()
            } catch (e: Exception) {
                estadoVerificaciones = "Error: ${e.message}"
                estanActivas = false
            }
            delay(5000)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (estanActivas) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sistema de Notificaciones Push",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (estanActivas) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                    )
                    Text(
                        text = if (estanActivas) "Activo" else "Inactivo",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (estanActivas) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = estadoVerificaciones,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Verificación cada 30 segundos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { 
                        try {
                            if (estanActivas) {
                                notificationScheduler.detenerVerificacionesPeriodicas()
                            } else {
                                notificationScheduler.iniciarVerificacionesPeriodicas()
                            }
                        } catch (e: Exception) {
                            // Manejar error
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = if (estanActivas) "Detener" else "Iniciar")
                }

                OutlinedButton(
                    onClick = { 
                        try {
                            notificationScheduler.ejecutarVerificacionInmediata()
                        } catch (e: Exception) {
                            // Manejar error
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Verificar Ahora")
                }
            }
        }
    }
}
