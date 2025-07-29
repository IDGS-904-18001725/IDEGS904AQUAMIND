package com.example.idegs904aquamind.features.graficos.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.Evento
import com.example.idegs904aquamind.data.model.TipoPeriodo
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ConsumoChart(
    eventos: List<Evento>,
    tipoPeriodo: TipoPeriodo,
    modifier: Modifier = Modifier
) {
    if (eventos.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "No hay datos para mostrar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
        return
    }

    val chartEntries = eventos.mapIndexed { index, evento ->
        entryOf(
            x = index.toFloat(),
            y = evento.consumo_total?.toFloat() ?: 0f
        )
    }

    val chartEntryModelProducer = remember { ChartEntryModelProducer(chartEntries) }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Consumo de Agua",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        
        Chart(
            chart = columnChart(),
            chartModelProducer = chartEntryModelProducer,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
        )
        
        // Resumen de datos
        ResumenConsumo(eventos = eventos, tipoPeriodo = tipoPeriodo)
    }
}

@Composable
fun ConsumoLineChart(
    eventos: List<Evento>,
    tipoPeriodo: TipoPeriodo,
    modifier: Modifier = Modifier
) {
    if (eventos.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.WaterDrop,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "No hay datos para mostrar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }
        return
    }

    val chartEntries = eventos.mapIndexed { index, evento ->
        entryOf(
            x = index.toFloat(),
            y = evento.consumo_total?.toFloat() ?: 0f
        )
    }

    val chartEntryModelProducer = remember { ChartEntryModelProducer(chartEntries) }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Tendencia de Consumo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        
        Chart(
            chart = lineChart(),
            chartModelProducer = chartEntryModelProducer,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
        )
    }
}

@Composable
fun ResumenConsumo(
    eventos: List<Evento>,
    tipoPeriodo: TipoPeriodo,
    modifier: Modifier = Modifier
) {
    val totalConsumo = eventos.sumOf { it.consumo_total?.toDouble() ?: 0.0 }
    val promedioConsumo = if (eventos.isNotEmpty()) totalConsumo / eventos.size else 0.0
    val maxConsumo = eventos.maxOfOrNull { it.consumo_total?.toDouble() ?: 0.0 } ?: 0.0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Resumen de Consumo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EstadisticaItem(
                    titulo = "Total",
                    valor = "${String.format("%.1f", totalConsumo)} lt"
                )
                EstadisticaItem(
                    titulo = "Promedio",
                    valor = "${String.format("%.1f", promedioConsumo)} lt"
                )
                EstadisticaItem(
                    titulo = "Máximo",
                    valor = "${String.format("%.1f", maxConsumo)} lt"
                )
            }
        }
    }
}

@Composable
fun EstadisticaItem(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0277BD)
        )
    }
} 