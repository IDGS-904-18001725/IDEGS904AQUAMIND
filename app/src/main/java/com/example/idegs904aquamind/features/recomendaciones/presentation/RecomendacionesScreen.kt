package com.example.idegs904aquamind.features.recomendaciones.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.idegs904aquamind.data.model.Recomendacion
import com.example.idegs904aquamind.features.recomendaciones.presentation.components.RecomendacionCard

@Composable
fun RecomendacionesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel = remember { RecomendacionesViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()
    
    var selectedRecomendacion by remember { mutableStateOf<Recomendacion?>(null) }
    var showModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadRecomendaciones()
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is RecomendacionesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RecomendacionesUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.recomendaciones) { recomendacion ->
                        RecomendacionCard(
                            recomendacion = recomendacion,
                            onClick = {
                                selectedRecomendacion = recomendacion
                                showModal = true
                            }
                        )
                    }
                }
            }
            is RecomendacionesUiState.Error -> {
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
            is RecomendacionesUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay recomendaciones disponibles",
                        color = Color.Gray
                    )
                }
            }
        }
    }

    // Modal para mostrar detalles
    if (showModal && selectedRecomendacion != null) {
        RecomendacionModal(
            recomendacion = selectedRecomendacion!!,
            onDismiss = {
                showModal = false
                selectedRecomendacion = null
            }
        )
    }
}

@Composable
fun RecomendacionModal(
    recomendacion: Recomendacion,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Imagen si existe
                recomendacion.url_imagen?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "Imagen de recomendación",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(16.dp))
                }

                // Título
                Text(
                    text = recomendacion.recomendacion,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(Modifier.height(12.dp))
                
                // Descripción
                Text(
                    text = recomendacion.descripcion,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Spacer(Modifier.height(16.dp))
                
                // Botón cerrar
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0277BD)
                    )
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
} 