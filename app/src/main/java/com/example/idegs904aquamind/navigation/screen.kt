package com.example.idegs904aquamind.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Define las rutas de navegación de la app.
 *
 * Cada objeto hereda de Screen y especifica su ruta única.
 * Para pantallas con bottom nav, incluye icono y label.
 */
sealed class Screen(
    val route: String, 
    val title: String = "",
    val icon: ImageVector? = null,
    val label: String = ""
) {
    object Login : Screen("login", "Iniciar sesión")
    object Dashboard : Screen("dashboard", "Dashboard")
    object Controllers : Screen("controllers", "Controles", Icons.Default.AccountTree, "Controles")
    object Notifications : Screen("notifications", "Notificaciones", Icons.Default.Notifications, "Notificaciones")
    object History : Screen("history", "Historial", Icons.Default.History, "Historial")
    object Settings : Screen("settings", "Configuración", Icons.Default.Settings, "Configuración")
    object Eventos : Screen("eventos", "Eventos", Icons.Default.Timeline, "Eventos")
}
