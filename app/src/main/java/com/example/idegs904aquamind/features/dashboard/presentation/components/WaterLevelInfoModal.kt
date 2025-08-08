package com.example.idegs904aquamind.features.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.idegs904aquamind.data.model.WaterLevelResponse
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WaterLevelInfoModal(
    waterLevelData: WaterLevelResponse,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
                            Column(
                    modifier = Modifier.padding(28.dp)
                ) {
                // Header mejorado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = "Nivel de Agua",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Información Detallada",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Datos completos del sistema",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                                    Spacer(modifier = Modifier.height(28.dp))
                    
                    // Contenido scrolleable
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                ) {
                    // Estado General
                    InfoSection(
                        title = "Estado General",
                        items = listOf(
                            "Nivel" to waterLevelData.interpretacion.nivel,
                            "Descripción" to waterLevelData.interpretacion.descripcion,
                            "Recomendación" to waterLevelData.interpretacion.recomendacion,
                            "Porcentaje Actual" to "${waterLevelData.data.porcentaje_agua}%"
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Medición
                    InfoSection(
                        title = "Medición",
                        items = listOf(
                            "Distancia" to waterLevelData.interpretacion.medicion.distancia,
                            "Interpretación" to waterLevelData.interpretacion.medicion.interpretacion,
                            "Fecha" to formatearFecha(waterLevelData.data.fecha)
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Dispositivos
                    InfoSection(
                        title = "Estado de Dispositivos",
                        items = listOf(
                            "Bomba" to "${waterLevelData.interpretacion.dispositivos.bomba.estado} - ${waterLevelData.interpretacion.dispositivos.bomba.descripcion}",
                            "Compuerta" to "${waterLevelData.interpretacion.dispositivos.compuerta.estado} - ${waterLevelData.interpretacion.dispositivos.compuerta.descripcion}"
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Datos Técnicos
                    InfoSection(
                        title = "Datos Técnicos",
                        items = listOf(
                            "Distancia (cm)" to waterLevelData.data.distancia.toString(),
                            "Desnivel" to if (waterLevelData.data.desnivel == "True") "Sí" else "No",
                            "Bomba Activa" to if (waterLevelData.data.bomba == "True") "Sí" else "No",
                            "Compuerta Abierta" to if (waterLevelData.data.compuerta == "True") "Sí" else "No",
                            "Estado del Nivel" to waterLevelData.data.nivel_estado,
                            "ID Nivel" to waterLevelData.data.id_nivel.toString()
                        )
                    )
                    

                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                // Botón de cerrar mejorado
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Cerrar",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    items: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                if (label != items.last().first) {
                    Divider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}

private fun formatearFecha(fechaString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(fechaString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        fechaString
    }
}
