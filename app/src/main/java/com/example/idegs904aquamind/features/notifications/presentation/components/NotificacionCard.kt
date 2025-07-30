package com.example.idegs904aquamind.features.notifications.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.Notificacion
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificacionCard(
    notificacion: Notificacion,
    onNotificacionClick: (Notificacion) -> Unit,
    modifier: Modifier = Modifier
) {
    val isLeida = notificacion.id_estatus == 2
    val backgroundColor = if (isLeida) Color(0xFFF5F5F5) else Color(0xFFE3F2FD)
    val textColor = if (isLeida) Color.Gray else Color.Black
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        onClick = { onNotificacionClick(notificacion) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isLeida) Icons.Filled.Notifications else Icons.Filled.NotificationsActive,
                contentDescription = "Notificación",
                tint = if (isLeida) Color.Gray else Color(0xFF0277BD),
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notificacion.notificacion,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isLeida) FontWeight.Normal else FontWeight.Bold,
                    color = textColor
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = notificacion.mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor.copy(alpha = 0.7f),
                    maxLines = 2
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = formatFecha(notificacion.fecha_notificacion),
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.6f)
                )
            }
            if (!isLeida) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = Color(0xFF0277BD),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

private fun formatFecha(fechaString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fecha = inputFormat.parse(fechaString)
        fecha?.let { outputFormat.format(it) } ?: fechaString
    } catch (e: Exception) {
        fechaString
    }
} 