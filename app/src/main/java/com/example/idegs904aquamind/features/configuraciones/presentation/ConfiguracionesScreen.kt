package com.example.idegs904aquamind.features.configuraciones.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.idegs904aquamind.data.model.Configuracion
import com.example.idegs904aquamind.features.configuraciones.presentation.components.ConfiguracionInput

@Composable
fun ConfiguracionesScreen(
    viewModel: ConfiguracionesViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0277BD))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuraciones",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Configuraciones del Sistema",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Configuración de niveles de agua",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
        
        // Contenido con scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón de recargar
            if (state.error != null) {
                ErrorCard(
                    message = state.error!!,
                    onRetry = { viewModel.cargarConfiguraciones() }
                )
            }
            
            // Loading state
            if (state.isLoading) {
                LoadingCard()
            } else {
                // Configuraciones
                state.configuraciones
                    .filter { it.id_configuracion in 1..4 }
                    .forEach { configuracion ->
                        when (configuracion.id_configuracion) {
                            1 -> ConfiguracionInput(
                                configuracion = configuracion,
                                onValueChange = { valor ->
                                    viewModel.actualizarConfiguracion(configuracion.id_configuracion, valor)
                                },
                                onSave = {
                                    // El valor se actualiza a través del onValueChange
                                },
                                // Sin validaciones para el primer input (solo numérico)
                                isUpdating = state.isUpdating,
                                unidadMedida = "CM"
                            )
                            2 -> ConfiguracionInput(
                                configuracion = configuracion,
                                onValueChange = { valor ->
                                    viewModel.actualizarConfiguracion(configuracion.id_configuracion, valor)
                                },
                                onSave = {
                                    // El valor se actualiza a través del onValueChange
                                },
                                maxValue = 100.0, // Nivel Alto no puede superar 100%
                                isUpdating = state.isUpdating,
                                unidadMedida = "%"
                            )
                            3 -> ConfiguracionInput(
                                configuracion = configuracion,
                                onValueChange = { valor ->
                                    viewModel.actualizarConfiguracion(configuracion.id_configuracion, valor)
                                },
                                onSave = {
                                    // El valor se actualiza a través del onValueChange
                                },
                                maxValue = state.configuraciones.find { it.id_configuracion == 2 }?.valor?.toDoubleOrNull() ?: 100.0,
                                isUpdating = state.isUpdating,
                                unidadMedida = "%"
                            )
                            4 -> ConfiguracionInput(
                                configuracion = configuracion,
                                onValueChange = { valor ->
                                    viewModel.actualizarConfiguracion(configuracion.id_configuracion, valor)
                                },
                                onSave = {
                                    // El valor se actualiza a través del onValueChange
                                },
                                maxValue = state.configuraciones.find { it.id_configuracion == 3 }?.valor?.toDoubleOrNull() ?: 100.0,
                                isUpdating = state.isUpdating,
                                unidadMedida = "%"
                            )
                        }
                    }
            }
            
            // Error de actualización
            if (state.updateError != null) {
                ErrorCard(
                    message = state.updateError!!,
                    onRetry = { viewModel.limpiarErrores() }
                )
            }
            
            // Espacio adicional al final
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun LoadingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando configuraciones...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ErrorCard(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Error",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reintentar"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reintentar")
            }
        }
    }
} 