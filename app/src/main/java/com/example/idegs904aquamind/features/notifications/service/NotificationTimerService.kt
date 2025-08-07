package com.example.idegs904aquamind.features.notifications.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.features.notifications.data.NotificacionesRepository
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Servicio que verifica notificaciones cada 30 segundos usando Timer.
 * Alternativa a WorkManager para intervalos cortos.
 */
class NotificationTimerService : Service() {

    companion object {
        private const val TAG = "NotificationTimerService"
        private const val NOTIFICATION_STATUS_NO_LEIDA = 1
        private const val FREQUENCY_SECONDS = 30L
    }

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var timer: Timer? = null
    private val repository = NotificacionesRepository(this)
    private val sessionManager = SessionManager(this)
    private val notificationHelper = NotificationHelper(this)

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Servicio de notificaciones creado")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Servicio iniciado")
        iniciarVerificaciones()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Servicio destruido")
        detenerVerificaciones()
        serviceScope.cancel()
    }

    /**
     * Inicia las verificaciones periódicas
     */
    private fun iniciarVerificaciones() {
        try {
            Log.d(TAG, "Iniciando verificaciones cada $FREQUENCY_SECONDS segundos")
            
            // Cancelar timer existente si hay uno
            timer?.cancel()
            
            // Crear nuevo timer
            timer = Timer().apply {
                scheduleAtFixedRate(
                    object : TimerTask() {
                        override fun run() {
                            serviceScope.launch {
                                verificarNotificaciones()
                            }
                        }
                    },
                    0, // Ejecutar inmediatamente
                    TimeUnit.SECONDS.toMillis(FREQUENCY_SECONDS) // Repetir cada 30 segundos
                )
            }
            
            Log.d(TAG, "Timer programado exitosamente")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error iniciando verificaciones: ${e.message}", e)
        }
    }

    /**
     * Detiene las verificaciones
     */
    private fun detenerVerificaciones() {
        try {
            Log.d(TAG, "Deteniendo verificaciones")
            timer?.cancel()
            timer = null
            Log.d(TAG, "Verificaciones detenidas")
        } catch (e: Exception) {
            Log.e(TAG, "Error deteniendo verificaciones: ${e.message}", e)
        }
    }

    /**
     * Verifica si hay nuevas notificaciones
     */
    private suspend fun verificarNotificaciones() {
        try {
            Log.d(TAG, "=== VERIFICANDO NOTIFICACIONES ===")
            
            // Verificar si hay sesión activa
            val token = sessionManager.getToken()
            if (token.isNullOrEmpty()) {
                Log.d(TAG, "No hay sesión activa, saltando verificación")
                return
            }

            Log.d(TAG, "Sesión activa encontrada, verificando notificaciones...")

            // Obtener notificaciones no leídas
            val notificacionesNoLeidas = repository.getNotificacionesPorEstatus(NOTIFICATION_STATUS_NO_LEIDA)
            Log.d(TAG, "Encontradas ${notificacionesNoLeidas.size} notificaciones no leídas")

            // Verificar si hay notificaciones nuevas
            val notificacionesNuevas = obtenerNotificacionesNuevas(notificacionesNoLeidas)
            
            if (notificacionesNuevas.isNotEmpty()) {
                Log.d(TAG, "🎉 Encontradas ${notificacionesNuevas.size} notificaciones nuevas")
                
                // Mostrar notificación para cada nueva notificación
                notificacionesNuevas.forEach { notificacion ->
                    notificationHelper.mostrarNotificacion(notificacion)
                    Log.d(TAG, "Notificación mostrada: ${notificacion.notificacion}")
                }
                
                // Actualizar timestamp de última verificación
                actualizarTimestampVerificacion()
            } else {
                Log.d(TAG, "No hay notificaciones nuevas")
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en verificación de notificaciones: ${e.message}", e)
        }
    }

    /**
     * Filtra las notificaciones que son realmente nuevas
     */
    private suspend fun obtenerNotificacionesNuevas(
        notificaciones: List<Notificacion>
    ): List<Notificacion> {
        val timestampUltimaVerificacion = obtenerTimestampUltimaVerificacion()
        
        return notificaciones.filter { notificacion ->
            if (timestampUltimaVerificacion == 0L) {
                Log.d(TAG, "Primera verificación, todas las notificaciones son nuevas")
                true
            } else {
                val fechaNotificacion = parsearFechaNotificacion(notificacion.fecha_notificacion)
                val esNueva = fechaNotificacion > timestampUltimaVerificacion
                if (esNueva) {
                    Log.d(TAG, "Notificación nueva encontrada: ${notificacion.notificacion}")
                }
                esNueva
            }
        }
    }

    /**
     * Parsea la fecha de notificación
     */
    private fun parsearFechaNotificacion(fechaString: String): Long {
        return try {
            val formatter = java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
            val zonedDateTime = java.time.ZonedDateTime.parse(fechaString, formatter)
            zonedDateTime.toInstant().toEpochMilli()
        } catch (e: Exception) {
            Log.e(TAG, "Error parseando fecha: $fechaString", e)
            System.currentTimeMillis()
        }
    }

    /**
     * Obtiene el timestamp de la última verificación
     */
    private fun obtenerTimestampUltimaVerificacion(): Long {
        val prefs = getSharedPreferences("notification_prefs", MODE_PRIVATE)
        return prefs.getLong("last_check_timestamp", 0L)
    }

    /**
     * Actualiza el timestamp de la última verificación
     */
    private fun actualizarTimestampVerificacion() {
        val prefs = getSharedPreferences("notification_prefs", MODE_PRIVATE)
        prefs.edit().putLong("last_check_timestamp", System.currentTimeMillis()).apply()
        Log.d(TAG, "Timestamp de verificación actualizado")
    }
}
