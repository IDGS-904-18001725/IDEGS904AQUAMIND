package com.example.idegs904aquamind.features.notifications.service

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.features.notifications.data.NotificacionesRepository

/**
 * Scheduler simple que usa coroutines para verificaciones cada 30 segundos.
 * Más seguro que usar servicios para evitar crashes.
 */
class SimpleNotificationScheduler(private val context: Context) {

    companion object {
        private const val TAG = "SimpleNotificationScheduler"
        private const val FREQUENCY_SECONDS = 30L
    }

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val repository = NotificacionesRepository(context)
    private val sessionManager = SessionManager(context)
    private val notificationHelper = NotificationHelper(context)

    /**
     * Inicia las verificaciones periódicas usando coroutines
     */
    fun iniciarVerificacionesPeriodicas() {
        try {
            Log.d(TAG, "Iniciando verificaciones periódicas cada $FREQUENCY_SECONDS segundos")
            
            // Cancelar job existente si hay uno
            job?.cancel()
            
            // Crear nuevo job
            job = scope.launch {
                while (isActive) {
                    try {
                        verificarNotificaciones()
                        delay(FREQUENCY_SECONDS * 1000) // Esperar 30 segundos
                    } catch (e: Exception) {
                        Log.e(TAG, "Error en verificación: ${e.message}", e)
                        delay(5000) // Esperar 5 segundos antes de reintentar
                    }
                }
            }
            
            Log.d(TAG, "Verificaciones periódicas iniciadas exitosamente")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error iniciando verificaciones periódicas: ${e.message}", e)
        }
    }

    /**
     * Detiene las verificaciones periódicas
     */
    fun detenerVerificacionesPeriodicas() {
        try {
            Log.d(TAG, "Deteniendo verificaciones periódicas")
            
            job?.cancel()
            job = null
            
            Log.d(TAG, "Verificaciones periódicas detenidas")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error deteniendo verificaciones periódicas: ${e.message}", e)
        }
    }

    /**
     * Verifica si las verificaciones están activas
     */
    fun estanVerificacionesActivas(): Boolean {
        return job?.isActive == true
    }

    /**
     * Obtiene información sobre el estado actual de las verificaciones
     */
    fun obtenerEstadoVerificaciones(): String {
        return if (estanVerificacionesActivas()) {
            "Coroutine Activa, Frecuencia: $FREQUENCY_SECONDS segundos"
        } else {
            "Coroutine Inactiva"
        }
    }

    /**
     * Ejecuta una verificación inmediata (para pruebas)
     */
    fun ejecutarVerificacionInmediata() {
        try {
            Log.d(TAG, "Ejecutando verificación inmediata")
            
            scope.launch {
                verificarNotificaciones()
            }
            
            Log.d(TAG, "Verificación inmediata ejecutada")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error ejecutando verificación inmediata: ${e.message}", e)
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
            val notificacionesNoLeidas = repository.getNotificacionesPorEstatus(1)
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
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        return prefs.getLong("last_check_timestamp", 0L)
    }

    /**
     * Actualiza el timestamp de la última verificación
     */
    private fun actualizarTimestampVerificacion() {
        val prefs = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_check_timestamp", System.currentTimeMillis()).apply()
        Log.d(TAG, "Timestamp de verificación actualizado")
    }
}
