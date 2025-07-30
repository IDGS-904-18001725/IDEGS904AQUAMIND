package com.example.idegs904aquamind.features.soporte.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Support
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SoporteScreen(modifier: Modifier = Modifier) {
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
                    imageVector = Icons.Default.Support,
                    contentDescription = "Soporte",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Soporte Técnico",
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
            // Contacto directo
            SoporteCard(
                titulo = "Contacto Directo",
                descripcion = "Comunícate con nuestro equipo técnico",
                accion = "Llamar"
            )
            
            // Chat en vivo
            SoporteCard(
                titulo = "Chat en Vivo",
                descripcion = "Conversa con un especialista en tiempo real",
                accion = "Iniciar Chat"
            )
            
            // Ticket de soporte
            SoporteCard(
                titulo = "Crear Ticket",
                descripcion = "Reporta un problema o solicita ayuda",
                accion = "Crear Ticket"
            )
            
            // FAQ
            SoporteCard(
                titulo = "Preguntas Frecuentes",
                descripcion = "Encuentra respuestas rápidas",
                accion = "Ver FAQ"
            )
            
            // Documentación
            SoporteCard(
                titulo = "Documentación",
                descripcion = "Manuales y guías de usuario",
                accion = "Ver Docs"
            )
        }
    }
}

@Composable
fun SoporteCard(
    titulo: String,
    descripcion: String,
    accion: String
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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0277BD)
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(accion)
            }
        }
    }
} 