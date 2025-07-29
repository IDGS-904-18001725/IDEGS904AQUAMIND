package com.example.idegs904aquamind.navigation

/**
 * Define las rutas de navegación de la app.
 *
 * Cada objeto hereda de Screen y especifica su ruta única.
 */
sealed class Screen(val route: String, val title: String = "") {
    object Login : Screen("login", "Iniciar sesión")
    object Dashboard : Screen("dashboard", "Dashboard")

}
