package com.example.idegs904aquamind
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.features.notifications.service.SimpleNotificationScheduler
import com.example.idegs904aquamind.features.notifications.utils.NotificationPermissionHelper
import com.example.idegs904aquamind.features.configuraciones.data.ConfiguracionesLocalManager
import com.example.idegs904aquamind.navigation.AppNavHost
import com.example.idegs904aquamind.navigation.Screen
import com.example.idegs904aquamind.ui.theme.AquaMindTheme

/**
 * Actividad principal que hospeda la navegación de la app.
 * - Inicia siempre en Login.
 * - Configura NavController y AppNavHost.
 */
class MainActivity : FragmentActivity() {
    private lateinit var notificationScheduler: SimpleNotificationScheduler
    private lateinit var permissionHelper: NotificationPermissionHelper
    private lateinit var configuracionesLocalManager: ConfiguracionesLocalManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar helpers
        notificationScheduler = SimpleNotificationScheduler(this)
        permissionHelper = NotificationPermissionHelper(this)
        configuracionesLocalManager = ConfiguracionesLocalManager(this)

        // Solicitar permisos de notificación
        permissionHelper.solicitarPermisosNotificacion(this) { isGranted ->
            if (isGranted) {
                // Los permisos fueron otorgados, el sistema de notificaciones funcionará
            } else {
                // Los permisos fueron denegados, mostrar mensaje o manejar
            }
        }

        // Siempre iniciar en pantalla de Login
        val startDestination = Screen.Login.route

        setContent {
            AquaMindTheme {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    onLoginSuccess = {
                        // Verificar configuración antes de iniciar actualizaciones automáticas
                        try {
                            val actualizacionesHabilitadas = configuracionesLocalManager.getActualizacionesAutomaticas()
                            if (actualizacionesHabilitadas) {
                                val frecuencia = configuracionesLocalManager.getFrecuenciaNotificaciones()
                                notificationScheduler.iniciarVerificacionesPeriodicas()
                                android.util.Log.d("MainActivity", "Actualizaciones automáticas iniciadas cada $frecuencia segundos")
                            } else {
                                android.util.Log.d("MainActivity", "Actualizaciones automáticas desactivadas por configuración")
                            }
                        } catch (e: Exception) {
                            // Log del error pero no crashear la app
                            android.util.Log.e("MainActivity", "Error iniciando notificaciones: ${e.message}", e)
                        }
                    }
                )
            }
        }
    }
}
