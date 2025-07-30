package com.example.idegs904aquamind.features.configuraciones.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
        
        // Contenido con scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
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
            
            // Espacio adicional al final para evitar que el último elemento se corte
            Spacer(modifier = Modifier.height(20.dp))
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
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Switch(
                        checked = false,
                        onCheckedChange = { /* TODO */ }
                    )
                }
                
                // Separador entre elementos (excepto el último)
                if (index < items.size - 1) {
                    Divider(
                        modifier = Modifier.padding(top = 8.dp),
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
} 