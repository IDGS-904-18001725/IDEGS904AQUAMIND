package com.example.idegs904aquamind.features.soporte.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SoporteScreen(modifier: Modifier = Modifier) {
    var showModal by remember { mutableStateOf(false) }
    
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
                accion = "Llamar",
                onClick = { showModal = true }
            )
            
            // Chat en vivo
            SoporteCard(
                titulo = "Chat en Vivo",
                descripcion = "Conversa con un especialista en tiempo real",
                accion = "Iniciar Chat",
                onClick = { showModal = true }
            )
            
            // Ticket de soporte
            SoporteCard(
                titulo = "Crear Ticket",
                descripcion = "Reporta un problema o solicita ayuda",
                accion = "Crear Ticket",
                onClick = { showModal = true }
            )
            
            // FAQ
            SoporteCard(
                titulo = "Preguntas Frecuentes",
                descripcion = "Encuentra respuestas rápidas",
                accion = "Ver FAQ",
                onClick = { showModal = true }
            )
            
            // Documentación
            SoporteCard(
                titulo = "Documentación",
                descripcion = "Manuales y guías de usuario",
                accion = "Ver Docs",
                onClick = { showModal = true }
            )
        }
    }
    
    // Modal de "En Construcción"
    if (showModal) {
        ConstruccionModal(
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun SoporteCard(
    titulo: String,
    descripcion: String,
    accion: String,
    onClick: () -> Unit
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
            Column(
                modifier = Modifier.weight(1f)
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
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0277BD)
                )
            ) {
                Text(accion)
            }
        }
    }
}

@Composable
fun ConstruccionModal(
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
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Construction,
                    contentDescription = "En Construcción",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFFFF9800)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "En Construcción",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Esta funcionalidad se encuentra en desarrollo. Pronto estará disponible para ti.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0277BD)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entendido")
                }
            }
        }
    }
} 