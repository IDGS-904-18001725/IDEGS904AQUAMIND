package com.example.idegs904aquamind.navigation

/**
 * Define las rutas de navegación de la app.
 *
 * Cada objeto hereda de Screen y especifica su ruta única.
 */
sealed class Screen(val route: String) {

    /**
     * Pantalla de login donde el usuario ingresa credenciales.
     */
    object Login : Screen("login")

    /**
     * Pantalla principal tras el login (ejemplo placeholder).
     */
    object Home : Screen("home")

    // Aquí puedes agregar más rutas para otros módulos (Settings, Profile, etc.)
}
