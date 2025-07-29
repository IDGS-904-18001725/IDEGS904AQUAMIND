package com.example.idegs904aquamind.navigation.components


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.navigation.Screen


// 2. Extensión de NavController para navegar en modo "single‑top" y restaurar estado
fun NavController.navigateSingleTop(route: String) {
    this.navigate(route) {
        // Evita crear duplicados en la pila si ya estamos en esa pantalla
        launchSingleTop = true
        // Restaura el estado anterior de la pantalla si existía
        restoreState = true
        // Hace popUpTo la ruta inicial del grafo y guarda su estado
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
    }
}

fun NavController.navigateToRoot(route: String) {
    this.navigate(route) {
        // No duplicar la misma pestaña
        launchSingleTop = true
        // Limpia cualquier destino apilado sobre esta ruta
        popUpTo(route) {
            inclusive = false
        }
        // No restaurar estado anterior (evita volver a Users, etc.)
        restoreState = false
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    // 3. Obtenemos la ruta actual de la back stack para marcar el ítem seleccionado
    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value
        ?.destination
        ?.route

    // 4. Lista de pantallas que aparecerán en la barra
    val items = listOf(
        Screen.Controllers,
        Screen.Notifications,
        Screen.History,
        Screen.Settings
    )

    // 5. Componente de Material3: la barra de navegación inferior
    NavigationBar(
        // Usamos el color container primario definido en el tema de la app
        containerColor = MaterialTheme.colorScheme.primary,
        // Para que no quede debajo de la barra de sistema en dispositivos con gestos
        modifier = Modifier
            .height(70.dp)
            .navigationBarsPadding()
    ) {
        // 6. Por cada pantalla, creamos un NavigationBarItem
        items.forEach { screen ->
            val selected = currentRoute == screen.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    // Usamos la función extendida para una navegación más robusta
                    navController.navigateToRoot(screen.route)
                },
                icon = {
                    // Ícono de la pantalla
                    Icon(
                        imageVector    = screen.icon!!,
                        contentDescription = screen.label
                    )
                },
                // Sólo mostramos la etiqueta en el ítem activo
                alwaysShowLabel = selected,
                // Personalizamos colores según el estado seleccionado/no seleccionado
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                )
            )
        }
    }
}
