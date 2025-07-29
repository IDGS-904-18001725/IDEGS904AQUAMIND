package com.example.idegs904aquamind.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definición de la paleta de colores AquaMind para modo claro
private val LightColors = lightColorScheme(
    primary = Color(0xFF0277BD),      // Azul oscuro
    onPrimary = Color.White,
    secondary = Color(0xFF81D4FA),    // Azul claro
    onSecondary = Color.White,
    background = Color(0xFFE1F5FE),   // Fondo muy claro
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White
)

// (Opcional) Paleta para modo oscuro
private val DarkColors = darkColorScheme(
    primary = Color(0xFF81D4FA),
    onPrimary = Color.Black,
    secondary = Color(0xFF0277BD),
    onSecondary = Color.Black,
    background = Color(0xFF002F47),
    onBackground = Color.White,
    surface = Color(0xFF003E5F),
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

/**
 * Tema de la aplicación AquaMind.
 * Aplica la paleta AquaMind y tipografía por defecto de Material3.
 *
 * @param darkTheme Si es true, usa paleta DarkColors; en caso contrario LightColors.
 * @param content Composable que renderiza la UI de la app.
 */
@Composable
fun AquaMindTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}
