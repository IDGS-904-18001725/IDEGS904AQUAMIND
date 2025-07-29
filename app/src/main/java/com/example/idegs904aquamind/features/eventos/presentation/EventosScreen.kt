package com.example.idegs904aquamind.features.eventos.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.TipoConsulta
import com.example.idegs904aquamind.data.model.TipoPeriodo
import com.example.idegs904aquamind.features.eventos.presentation.components.EventoCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun EventosScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember { EventosViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()
    val tipoConsulta by viewModel.tipoConsulta.collectAsState()
    val fechaInicio by viewModel.fechaInicio.collectAsState()
    val fechaFin by viewModel.fechaFin.collectAsState()
    val cantidadPeriodos by viewModel.cantidadPeriodos.collectAsState()
    val tipoPeriodo by viewModel.tipoPeriodo.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // Header con selector de consulta
        ConsultaSelector(
            tipoConsulta = tipoConsulta,
            onTipoConsultaChange = { viewModel.setTipoConsulta(it) }
        )

        // Campos dinámicos según tipo de consulta
        when (tipoConsulta) {
            TipoConsulta.INTERVALO -> {
                FechasSelector(
                    fechaInicio = fechaInicio,
                    fechaFin = fechaFin,
                    onFechasChange = { inicio, fin -> viewModel.setFechas(inicio, fin) }
                )
            }
            TipoConsulta.PERIODO -> {
                PeriodoSelector(
                    cantidad = cantidadPeriodos,
                    tipo = tipoPeriodo,
                    onPeriodoChange = { cantidad, tipo -> viewModel.setPeriodo(cantidad, tipo) }
                )
            }
        }

        // Botón de búsqueda
        Button(
            onClick = { viewModel.buscarEventos() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0277BD)
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Buscar Eventos")
        }

        // Lista de resultados
        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {
                is EventosUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is EventosUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.eventos) { evento ->
                            EventoCard(
                                evento = evento,
                                tipoConsulta = state.tipoConsulta
                            )
                        }
                    }
                }
                is EventosUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            color = Color.Red
                        )
                    }
                }
                is EventosUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron eventos",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConsultaSelector(
    tipoConsulta: TipoConsulta,
    onTipoConsultaChange: (TipoConsulta) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = tipoConsulta == TipoConsulta.INTERVALO,
            onClick = { onTipoConsultaChange(TipoConsulta.INTERVALO) },
            label = { Text("Intervalo") },
            modifier = Modifier.weight(1f)
        )
        FilterChip(
            selected = tipoConsulta == TipoConsulta.PERIODO,
            onClick = { onTipoConsultaChange(TipoConsulta.PERIODO) },
            label = { Text("Periodicidad") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FechasSelector(
    fechaInicio: String,
    fechaFin: String,
    onFechasChange: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Selecciona el intervalo de fechas",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = fechaInicio,
                onValueChange = { onFechasChange(it, fechaFin) },
                label = { Text("Fecha Inicio") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = fechaFin,
                onValueChange = { onFechasChange(fechaInicio, it) },
                label = { Text("Fecha Fin") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodoSelector(
    cantidad: Int,
    tipo: TipoPeriodo,
    onPeriodoChange: (Int, TipoPeriodo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Selecciona la periodicidad",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        
        // Campo de cantidad
        OutlinedTextField(
            value = cantidad.toString(),
            onValueChange = { 
                val newCantidad = it.toIntOrNull() ?: cantidad
                onPeriodoChange(newCantidad, tipo)
            },
            label = { Text("Cantidad de periodos") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(Modifier.height(12.dp))
        
        // Selector de tipo con chips
        Text(
            text = "Tipo de periodo:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TipoPeriodo.values().forEach { periodo ->
                FilterChip(
                    selected = tipo == periodo,
                    onClick = { onPeriodoChange(cantidad, periodo) },
                    label = { Text(periodo.label) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
} 