package com.example.idegs904aquamind
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.navigation.AppNavHost
import com.example.idegs904aquamind.navigation.Screen
import com.example.idegs904aquamind.ui.theme.AquaMindTheme

/**
 * Actividad principal que hospeda la navegación de la app.
 * - Inicia siempre en Login.
 * - Configura NavController y AppNavHost.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Siempre iniciar en pantalla de Login
        val startDestination = Screen.Login.route

        setContent {
            AquaMindTheme {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}
