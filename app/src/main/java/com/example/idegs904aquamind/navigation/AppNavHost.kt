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
import com.example.idegs904aquamind.features.eventos.presentation.EventosScreen
import com.example.idegs904aquamind.features.recomendaciones.presentation.RecomendacionesScreen
import com.example.idegs904aquamind.features.graficos.presentation.GraficosScreen
import com.example.idegs904aquamind.features.configuraciones.presentation.ConfiguracionesScreen
import com.example.idegs904aquamind.features.configuraciones.presentation.ConfiguracionesViewModel
import com.example.idegs904aquamind.features.configuraciones.presentation.ConfiguracionesViewModelFactory
import com.example.idegs904aquamind.features.reportes.presentation.ReportesScreen
import com.example.idegs904aquamind.features.mantenimiento.presentation.MantenimientoScreen
import com.example.idegs904aquamind.features.soporte.presentation.SoporteScreen
import com.example.idegs904aquamind.features.ayuda.presentation.AyudaScreen
import com.example.idegs904aquamind.features.perfil.presentation.PerfilScreen
import com.example.idegs904aquamind.navigation.components.BaseScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
    onLoginSuccess: () -> Unit = {}
) {
    NavHost(navController, startDestination) {

        // 1) Login: no usamos BaseScreen porque no lleva barras
        composable(Screen.Login.route) {
            LoginScreen { _ ->
                // Llamar al callback de login exitoso
                onLoginSuccess()
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

        // 6) Eventos
        composable(Screen.Eventos.route) {
            BaseScreen(
                screen = Screen.Eventos,
                navController = navController
            ) { padding ->
                EventosScreen(modifier = Modifier.padding(padding))
            }
        }

        // 7) Recomendaciones
        composable(Screen.Recomendaciones.route) {
            BaseScreen(
                screen = Screen.Recomendaciones,
                navController = navController
            ) { padding ->
                RecomendacionesScreen(modifier = Modifier.padding(padding))
            }
        }

        // 8) Gráficos
        composable(Screen.Graficos.route) {
            BaseScreen(
                screen = Screen.Graficos,
                navController = navController
            ) { padding ->
                GraficosScreen(modifier = Modifier.padding(padding))
            }
        }

        // Nuevas rutas del drawer
        // 9) Configuraciones
        composable(Screen.Configuraciones.route) {
            val context = LocalContext.current
            val viewModel: ConfiguracionesViewModel = viewModel(
                factory = ConfiguracionesViewModelFactory(context)
            )
            
            BaseScreen(
                screen = Screen.Configuraciones,
                navController = navController
            ) { padding ->
                ConfiguracionesScreen(
                    viewModel = viewModel,
                    modifier = Modifier.padding(padding)
                )
            }
        }

        // 10) Reportes
        composable(Screen.Reportes.route) {
            BaseScreen(
                screen = Screen.Reportes,
                navController = navController
            ) { padding ->
                ReportesScreen(modifier = Modifier.padding(padding))
            }
        }

        // 11) Mantenimiento
        composable(Screen.Mantenimiento.route) {
            BaseScreen(
                screen = Screen.Mantenimiento,
                navController = navController
            ) { padding ->
                MantenimientoScreen(modifier = Modifier.padding(padding))
            }
        }

        // 12) Soporte
        composable(Screen.Soporte.route) {
            BaseScreen(
                screen = Screen.Soporte,
                navController = navController
            ) { padding ->
                SoporteScreen(modifier = Modifier.padding(padding))
            }
        }

        // 13) Ayuda
        composable(Screen.Ayuda.route) {
            BaseScreen(
                screen = Screen.Ayuda,
                navController = navController
            ) { padding ->
                AyudaScreen(modifier = Modifier.padding(padding))
            }
        }

        // 14) Perfil - con manejo de navegación al login
        composable(Screen.Perfil.route) {
            BaseScreen(
                screen = Screen.Perfil,
                navController = navController
            ) { padding ->
                PerfilScreen(
                    modifier = Modifier.padding(padding),
                    onNavigateToLogin = {
                        // Limpiar todo el stack de navegación y ir al login
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

    }
}
