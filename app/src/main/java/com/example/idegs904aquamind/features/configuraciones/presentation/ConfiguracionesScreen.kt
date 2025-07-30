package com.example.idegs904aquamind.features.configuraciones.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ConfiguracionesScreen(modifier: Modifier = Modifier) {
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
                    text = "Configuraciones",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección de Notificaciones
            ConfiguracionSeccion(
                titulo = "Notificaciones",
                items = listOf(
                    "Alertas de consumo",
                    "Recordatorios de mantenimiento",
                    "Reportes semanales",
                    "Notificaciones push"
                )
            )
            
            // Sección de Tema
            ConfiguracionSeccion(
                titulo = "Apariencia",
                items = listOf(
                    "Tema claro",
                    "Tema oscuro",
                    "Tema automático"
                )
            )
            
            // Sección de Datos
            ConfiguracionSeccion(
                titulo = "Datos",
                items = listOf(
                    "Sincronización automática",
                    "Almacenamiento local",
                    "Exportar datos",
                    "Limpiar caché"
                )
            )
        }
    }
}

@Composable
fun ConfiguracionSeccion(
    titulo: String,
    items: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Switch(
                        checked = false,
                        onCheckedChange = { /* TODO */ }
                    )
                }
            }
        }
    }
} 