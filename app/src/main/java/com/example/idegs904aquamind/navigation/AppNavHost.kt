package com.example.idegs904aquamind.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.idegs904aquamind.auth.presentation.LoginScreen
import com.example.idegs904aquamind.features.dashboard.presentation.DashboardScreen
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

    }
}
