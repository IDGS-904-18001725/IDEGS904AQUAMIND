package com.example.idegs904aquamind.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.idegs904aquamind.auth.presentation.LoginScreen
import com.example.idegs904aquamind.features.dashboard.presentation.DashboardScreen
import com.example.idegs904aquamind.features.controllers.presentation.ControllersScreen
import com.example.idegs904aquamind.features.notifications.presentation.NotificationsScreen
import com.example.idegs904aquamind.features.history.presentation.HistoryScreen
import com.example.idegs904aquamind.features.settings.presentation.SettingsScreen
import com.example.idegs904aquamind.navigation.components.BaseScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(navController, startDestination) {

        // 1) Login: no usamos BaseScreen porque no lleva barras
        composable(Screen.Login.route) {
            LoginScreen { _ ->
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }

        // 2) Dashboard con Scaffold automático
        composable(Screen.Dashboard.route) {
            BaseScreen(
                screen = Screen.Dashboard,
                navController = navController
            ) { padding ->
                // Pasa padding al contenido
                DashboardScreen(modifier = Modifier.padding(padding))
            }
        }

        // 3) Controles
        composable(Screen.Controllers.route) {
            BaseScreen(
                screen = Screen.Controllers,
                navController = navController
            ) { padding ->
                ControllersScreen(modifier = Modifier.padding(padding))
            }
        }

        // 4) Notificaciones
        composable(Screen.Notifications.route) {
            BaseScreen(
                screen = Screen.Notifications,
                navController = navController
            ) { padding ->
                NotificationsScreen(modifier = Modifier.padding(padding))
            }
        }

        // 5) Historial
        composable(Screen.History.route) {
            BaseScreen(
                screen = Screen.History,
                navController = navController
            ) { padding ->
                HistoryScreen(modifier = Modifier.padding(padding))
            }
        }

        // 6) Configuraciones
        composable(Screen.Settings.route) {
            BaseScreen(
                screen = Screen.Settings,
                navController = navController
            ) { padding ->
                SettingsScreen(modifier = Modifier.padding(padding))
            }
        }

    }
}
