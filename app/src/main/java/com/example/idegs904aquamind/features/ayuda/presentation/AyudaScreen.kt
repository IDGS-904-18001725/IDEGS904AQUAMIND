package com.example.idegs904aquamind.features.ayuda.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AyudaScreen(modifier: Modifier = Modifier) {
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
                    imageVector = Icons.Default.Help,
                    contentDescription = "Ayuda",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Centro de Ayuda",
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
            // Guías rápidas
            AyudaCard(
                titulo = "Primeros Pasos",
                descripcion = "Aprende a usar AquaMind en 5 minutos",
                icon = Icons.Default.Help
            )
            
            AyudaCard(
                titulo = "Guía de Navegación",
                descripcion = "Conoce todas las funciones disponibles",
                icon = Icons.Default.Help
            )
            
            AyudaCard(
                titulo = "Solución de Problemas",
                descripcion = "Resuelve problemas comunes rápidamente",
                icon = Icons.Default.Help
            )
            
            AyudaCard(
                titulo = "Tutorial Interactivo",
                descripcion = "Aprende paso a paso con ejemplos",
                icon = Icons.Default.Help
            )
            
            AyudaCard(
                titulo = "Glosario Técnico",
                descripcion = "Definiciones de términos especializados",
                icon = Icons.Default.Help
            )
        }
    }
}

@Composable
fun AyudaCard(
    titulo: String,
    descripcion: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF0277BD)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0277BD)
                )
            ) {
                Text("Ver")
            }
        }
    }
} 