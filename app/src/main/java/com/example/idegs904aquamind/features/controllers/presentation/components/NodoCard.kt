package com.example.idegs904aquamind.features.controllers.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.features.controllers.data.DeviceCommandMapper

@Composable
fun NodoCard(
    nodo: Nodo,
    onToggleClick: (() -> Unit)? = null,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (nodo.id_estatus == 1)
                Color(0xFFE8F5E8) // Verde claro para activo
            else
                Color(0xFFFFEBEE) // Rojo claro para apagado
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.InvertColors, // Ícono temporal
                contentDescription = "Nodo de agua",
                tint = Color(0xFF0277BD),
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nodo.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = nodo.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "ID: ${nodo.id_nodo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            StatusChip(estatus = nodo.id_estatus)
            
            // Botón de toggle (solo mostrar si el dispositivo está mapeado)
            if (onToggleClick != null && isDeviceMapped(nodo.descripcion)) {
                Spacer(Modifier.width(8.dp))
                ToggleButton(
                    isActive = nodo.id_estatus == 1,
                    isLoading = isLoading,
                    onToggleClick = onToggleClick
                )
            }
        }
    }
}

private fun isDeviceMapped(descripcion: String): Boolean {
    val mapper = DeviceCommandMapper()
    return mapper.isDeviceMapped(descripcion)
}

@Composable
fun ToggleButton(
    isActive: Boolean,
    isLoading: Boolean,
    onToggleClick: () -> Unit
) {
    val backgroundColor = if (isActive) {
        Color(0xFF4CAF50) // Verde para activo
    } else {
        Color(0xFFF44336) // Rojo para inactivo
    }
    
    val icon = if (isActive) {
        Icons.Filled.Power
    } else {
        Icons.Filled.PowerOff
    }
    
    val iconTint = Color.White
    
    Button(
        onClick = onToggleClick,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.6f)
        ),
        modifier = Modifier.size(48.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = if (isActive) "Desactivar" else "Activar",
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun StatusChip(estatus: Int) {
    val (backgroundColor, textColor, text) = when (estatus) {
        1 -> Triple(Color(0xFF4CAF50), Color.White, "Activa")
        0 -> Triple(Color(0xFFF44336), Color.White, "Apagada")
        else -> Triple(Color.Gray, Color.White, "Desconocido")
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = textColor
        )
    }
} 