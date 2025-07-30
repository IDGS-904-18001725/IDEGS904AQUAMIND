package com.example.idegs904aquamind.features.ayuda.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Book
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// URLs de los videos de ayuda - Reemplazar con las URLs reales de tus videos
private object VideoUrls {
    const val PRIMEROS_PASOS = "https://youtu.be/r3cH7KYhgq8?si=8s8SGIXOKzpWuk-7"
    const val GUIA_NAVEGACION = "https://youtu.be/r3cH7KYhgq8?si=8s8SGIXOKzpWuk-7"
    const val SOLUCION_PROBLEMAS = "https://youtu.be/r3cH7KYhgq8?si=8s8SGIXOKzpWuk-7"
    const val TUTORIAL_INTERACTIVO = "https://youtu.be/r3cH7KYhgq8?si=8s8SGIXOKzpWuk-7"
    const val GLOSARIO_TECNICO = "https://youtu.be/r3cH7KYhgq8?si=8s8SGIXOKzpWuk-7"
}

@Composable
fun AyudaScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
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
            // Guías rápidas con videos
            AyudaCard(
                titulo = "Primeros Pasos",
                descripcion = "Aprende a usar AquaMind en 5 minutos",
                icon = Icons.Default.PlayArrow,
                videoUrl = VideoUrls.PRIMEROS_PASOS
            ) {
                abrirVideo(context, VideoUrls.PRIMEROS_PASOS, "Primeros Pasos")
            }
            
            AyudaCard(
                titulo = "Guía de Navegación",
                descripcion = "Conoce todas las funciones disponibles",
                icon = Icons.Default.Navigation,
                videoUrl = VideoUrls.GUIA_NAVEGACION
            ) {
                abrirVideo(context, VideoUrls.GUIA_NAVEGACION, "Guía de Navegación")
            }
            
            AyudaCard(
                titulo = "Solución de Problemas",
                descripcion = "Resuelve problemas comunes rápidamente",
                icon = Icons.Default.Build,
                videoUrl = VideoUrls.SOLUCION_PROBLEMAS
            ) {
                abrirVideo(context, VideoUrls.SOLUCION_PROBLEMAS, "Solución de Problemas")
            }
            
            AyudaCard(
                titulo = "Tutorial Interactivo",
                descripcion = "Aprende paso a paso con ejemplos",
                icon = Icons.Default.School,
                videoUrl = VideoUrls.TUTORIAL_INTERACTIVO
            ) {
                abrirVideo(context, VideoUrls.TUTORIAL_INTERACTIVO, "Tutorial Interactivo")
            }
            
            AyudaCard(
                titulo = "Glosario Técnico",
                descripcion = "Definiciones de términos especializados",
                icon = Icons.Default.Book,
                videoUrl = VideoUrls.GLOSARIO_TECNICO
            ) {
                abrirVideo(context, VideoUrls.GLOSARIO_TECNICO, "Glosario Técnico")
            }
        }
    }
}

@Composable
fun AyudaCard(
    titulo: String,
    descripcion: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    videoUrl: String,
    onVerClick: () -> Unit
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
                onClick = onVerClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0277BD)
                )
            ) {
                Text("Ver")
            }
        }
    }
}

/**
 * Función para abrir un video en el navegador o aplicación de YouTube
 * @param context Contexto de la aplicación
 * @param videoUrl URL del video a abrir
 * @param titulo Título del video para mostrar en mensajes de error
 */
private fun abrirVideo(context: android.content.Context, videoUrl: String, titulo: String) {
    try {
        // Extraer el ID del video de la URL youtu.be
        val videoId = if (videoUrl.contains("youtu.be/")) {
            videoUrl.substringAfter("youtu.be/").substringBefore("?")
        } else {
            null
        }
        
        // Intentar múltiples métodos para abrir el video
        var success = false
        
        if (videoId != null) {
            // Método 1: Intentar con el esquema de URI de YouTube
            try {
                val youtubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://$videoId"))
                youtubeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(youtubeIntent)
                success = true
            } catch (e: Exception) {
                // Continuar con el siguiente método
            }
            
            // Método 2: Si no funcionó, intentar con URL normal de YouTube
            if (!success) {
                try {
                    val normalYoutubeUrl = "https://www.youtube.com/watch?v=$videoId"
                    val normalIntent = Intent(Intent.ACTION_VIEW, Uri.parse(normalYoutubeUrl))
                    normalIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(normalIntent)
                    success = true
                } catch (e: Exception) {
                    // Continuar con el siguiente método
                }
            }
        }
        
        // Método 3: Como último recurso, intentar con la URL original
        if (!success) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                success = true
            } catch (e: Exception) {
                // Mostrar mensaje de error
                android.widget.Toast.makeText(
                    context,
                    "No se pudo abrir el video. Verifica que tengas YouTube instalado o un navegador web.",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
        
    } catch (e: Exception) {
        // Si ocurre algún error, mostrar mensaje informativo
        android.widget.Toast.makeText(
            context,
            "Error al abrir el video de $titulo: ${e.message}",
            android.widget.Toast.LENGTH_LONG
        ).show()
    }
} 