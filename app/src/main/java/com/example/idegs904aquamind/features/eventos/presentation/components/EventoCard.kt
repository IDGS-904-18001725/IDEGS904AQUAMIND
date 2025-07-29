package com.example.idegs904aquamind.features.eventos.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.Evento
import com.example.idegs904aquamind.data.model.TipoConsulta
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EventoCard(
    evento: Evento,
    tipoConsulta: TipoConsulta,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD) // Azul claro
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Timeline,
                contentDescription = "Evento",
                tint = Color(0xFF0277BD),
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = getTituloEvento(evento, tipoConsulta),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = getSubtituloEvento(evento, tipoConsulta),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(8.dp))
                ConsumoIndicator(
                    consumo = evento.consumo_total,
                    unidad = evento.unidad_medida
                )
            }
        }
    }
}

@Composable
fun ConsumoIndicator(
    consumo: Double?,
    unidad: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (consumo != null && unidad != null) {
                "${consumo} $unidad"
            } else {
                "Sin datos de consumo"
            },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (consumo != null) Color(0xFF0277BD) else Color.Gray
        )
    }
}

private fun getTituloEvento(evento: Evento, tipoConsulta: TipoConsulta): String {
    return when {
        evento.fecha != null -> {
            try {
                val fecha = LocalDate.parse(evento.fecha)
                fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            } catch (e: Exception) {
                evento.fecha ?: "Fecha no válida"
            }
        }
        evento.mes != null && evento.anio != null -> {
            "${evento.mes_nombre ?: "Mes"} ${evento.anio}"
        }
        evento.anio != null -> {
            evento.anio.toString()
        }
        else -> "Evento"
    }
}

private fun getSubtituloEvento(evento: Evento, tipoConsulta: TipoConsulta): String {
    return when {
        evento.fecha != null -> "Consumo diario"
        evento.mes != null -> "Consumo mensual"
        evento.anio != null -> "Consumo anual"
        else -> "Evento de consumo"
    }
} 