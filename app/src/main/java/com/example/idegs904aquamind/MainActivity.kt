package com.example.idegs904aquamind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.auth.presentation.LoginScreen
import com.example.idegs904aquamind.navigation.Screen

/**
 * Actividad principal que hospeda la navegación de la app.
 * - Determina la ruta inicial según exista o no un token en sesión.
 * - Configura NavController y NavHost con rutas Login y Home.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica si existe un token en sesión para definir la pantalla inicial
        val sessionManager = SessionManager(this)
        val startDestination = if (sessionManager.getToken() != null) {
            Screen.Home.route
        } else {
            Screen.Login.route
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        // Pantalla de login
                        composable(Screen.Login.route) {
                            LoginScreen { response ->
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        }
                        // Pantalla Home (placeholder)
                        composable(Screen.Home.route) {
                            Text(
                                text = "¡Bienvenido!",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
