package com.example.idegs904aquamind.features.controllers.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
    val isActive = nodo.id_estatus == 1
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isActive) 8.dp else 4.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = if (isActive) Color(0xFF4CAF50) else Color(0xFFF44336)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isActive) {
                            listOf(
                                Color(0xFFE8F5E8),
                                Color(0xFFC8E6C9)
                            )
                        } else {
                            listOf(
                                Color(0xFFFFEBEE),
                                Color(0xFFFFCDD2)
                            )
                        }
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono del dispositivo
                DeviceIcon(
                    descripcion = nodo.descripcion,
                    isActive = isActive,
                    modifier = Modifier.size(48.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Información del dispositivo
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = nodo.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = nodo.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Estado del dispositivo
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusIndicator(isActive = isActive)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isActive) "Activo" else "Inactivo",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (isActive) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                        )
                    }
                }
                
                // Botón de control
                if (onToggleClick != null && isDeviceMapped(nodo.descripcion)) {
                    Spacer(modifier = Modifier.width(12.dp))
                    ElegantToggleButton(
                        isActive = isActive,
                        isLoading = isLoading,
                        onToggleClick = onToggleClick
                    )
                }
            }
        }
    }
}

@Composable
private fun DeviceIcon(
    descripcion: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val icon = when {
        descripcion.contains("Válvula") -> Icons.Filled.WaterDrop
        descripcion.contains("Compuerta") -> Icons.Filled.Settings
        descripcion.contains("Relevador") -> Icons.Filled.ElectricBolt
        else -> Icons.Filled.DeviceHub
    }
    
    val iconColor = if (isActive) {
        Color(0xFF2E7D32)
    } else {
        Color(0xFFD32F2F)
    }
    
    Box(
        modifier = modifier
            .background(
                color = iconColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Dispositivo",
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun StatusIndicator(isActive: Boolean) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .background(
                color = if (isActive) Color(0xFF4CAF50) else Color(0xFFF44336),
                shape = RoundedCornerShape(4.dp)
            )
    )
}

@Composable
private fun ElegantToggleButton(
    isActive: Boolean,
    isLoading: Boolean,
    onToggleClick: () -> Unit
) {
    val backgroundColor = if (isActive) {
        Color(0xFF4CAF50)
    } else {
        Color(0xFFF44336)
    }
    
    val icon = if (isActive) {
        Icons.Filled.Power
    } else {
        Icons.Filled.PowerOff
    }
    
    Button(
        onClick = onToggleClick,
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.size(56.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                imageVector = icon,
                contentDescription = if (isActive) "Desactivar" else "Activar",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

private fun isDeviceMapped(descripcion: String): Boolean {
    val mapper = DeviceCommandMapper()
    return mapper.isDeviceMapped(descripcion)
} 