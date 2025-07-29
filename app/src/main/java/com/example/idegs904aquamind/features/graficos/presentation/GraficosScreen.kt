package com.example.idegs904aquamind.features.graficos.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.TipoPeriodo
import com.example.idegs904aquamind.features.graficos.presentation.components.ConsumoChart
import com.example.idegs904aquamind.features.graficos.presentation.components.ConsumoLineChart

@Composable
fun GraficosScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember { GraficosViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()
    val cantidadPeriodos by viewModel.cantidadPeriodos.collectAsState()
    val tipoPeriodo by viewModel.tipoPeriodo.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // Título de la sección
        Text(
            text = "Gráficos de Consumo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Selector de periodicidad
        PeriodoSelector(
            cantidad = cantidadPeriodos,
            tipo = tipoPeriodo,
            onPeriodoChange = { cantidad, tipo -> viewModel.setPeriodo(cantidad, tipo) }
        )

        // Botón de búsqueda
        Button(
            onClick = { viewModel.cargarDatosGraficos() },
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
            Text("Cargar Gráficos")
        }

        // Gráficos
        Box(modifier = Modifier.weight(1f)) {
            when (val state = uiState) {
                is GraficosUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is GraficosUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Gráfico de barras
                        ConsumoChart(
                            eventos = state.eventos,
                            tipoPeriodo = tipoPeriodo,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Gráfico de líneas
                        ConsumoLineChart(
                            eventos = state.eventos,
                            tipoPeriodo = tipoPeriodo,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                is GraficosUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.BarChart,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Red
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Error: ${state.message}",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                is GraficosUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.WaterDrop,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Gray
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "No hay datos de consumo",
                                color = Color.Gray,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "No se encontraron registros en el período seleccionado",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

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
            text = "Selecciona el período de análisis",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        
        // Selector de tipo con chips (Días, Meses y Años)
        Text(
            text = "Tipo de período:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Mostrar DÍAS, MESES y AÑOS
            TipoPeriodo.values().forEach { periodo ->
                FilterChip(
                    selected = tipo == periodo,
                    onClick = { onPeriodoChange(cantidad, periodo) },
                    label = { Text(periodo.label) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(Modifier.height(12.dp))
        
        // Campo de cantidad
        OutlinedTextField(
            value = cantidad.toString(),
            onValueChange = { 
                val newCantidad = it.toIntOrNull() ?: cantidad
                onPeriodoChange(newCantidad, tipo)
            },
            label = { 
                Text(
                    when (tipo) {
                        TipoPeriodo.DIAS -> "Cantidad de días"
                        TipoPeriodo.MESES -> "Cantidad de meses"
                        TipoPeriodo.ANIOS -> "Cantidad de años"
                    }
                ) 
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
} 