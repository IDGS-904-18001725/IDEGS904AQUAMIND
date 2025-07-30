package com.example.idegs904aquamind.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
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
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home, "Dashboard")
    object Controllers : Screen("controllers", "Controles", Icons.Default.AccountTree, "Controles")
    object Notifications : Screen("notifications", "Notificaciones", Icons.Default.Notifications, "Notificaciones")
    object History : Screen("history", "Historial", Icons.Default.History, "Historial")
    object Eventos : Screen("eventos", "Eventos", Icons.Default.Timeline, "Eventos")
    object Recomendaciones : Screen("recomendaciones", "Recomendaciones", Icons.Default.WaterDrop, "Recomendaciones")
    object Graficos : Screen("graficos", "Gráficos", Icons.Default.BarChart, "Gráficos")
    
    // Nuevas rutas para el drawer
    object Configuraciones : Screen("configuraciones", "Configuraciones")
    object Reportes : Screen("reportes", "Reportes")
    object Mantenimiento : Screen("mantenimiento", "Mantenimiento")
    object Soporte : Screen("soporte", "Soporte")
    object Ayuda : Screen("ayuda", "Ayuda")
    object Perfil : Screen("perfil", "Perfil")
}
