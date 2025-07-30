package com.example.idegs904aquamind.features.notifications.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.idegs904aquamind.data.model.Notificacion
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificacionModal(
    notificacion: Notificacion,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLeida = notificacion.id_estatus == 2
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icono grande
                Icon(
                    imageVector = if (isLeida) Icons.Filled.Notifications else Icons.Filled.NotificationsActive,
                    contentDescription = "Notificación",
                    tint = if (isLeida) Color.Gray else Color(0xFF0277BD),
                    modifier = Modifier.size(64.dp)
                )
                
                Spacer(Modifier.height(16.dp))
                
                // Título
                Text(
                    text = notificacion.notificacion,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(Modifier.height(8.dp))
                
                // Fecha
                Text(
                    text = formatFecha(notificacion.fecha_notificacion),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(Modifier.height(16.dp))
                
                // Mensaje
                Text(
                    text = notificacion.mensaje,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(Modifier.height(24.dp))
                
                // Botón cerrar
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cerrar",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar")
                }
            }
        }
    }
}

private fun formatFecha(fechaString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm", Locale.getDefault())
        val fecha = inputFormat.parse(fechaString)
        fecha?.let { outputFormat.format(it) } ?: fechaString
    } catch (e: Exception) {
        fechaString
    }
} 