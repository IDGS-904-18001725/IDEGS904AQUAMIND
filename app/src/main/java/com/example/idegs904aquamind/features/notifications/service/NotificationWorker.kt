package com.example.idegs904aquamind.features.notifications.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.features.notifications.data.NotificacionesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker que verifica cada 30 segundos si hay nuevas notificaciones no leídas.
 * Para demostración: verificación frecuente sin optimizaciones de batería.
 */
class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "NotificationWorker"
        private const val NOTIFICATION_STATUS_NO_LEIDA = 1
    }

    private val repository = NotificacionesRepository(context)
    private val sessionManager = SessionManager(context)
    private val notificationHelper = NotificationHelper(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando verificación de notificaciones...")
            
            // Verificar si hay sesión activa
            val token = sessionManager.getToken()
            if (token.isNullOrEmpty()) {
                Log.d(TAG, "No hay sesión activa, saltando verificación")
                return@withContext Result.success()
            }

            // Obtener notificaciones no leídas
            val notificacionesNoLeidas = repository.getNotificacionesPorEstatus(NOTIFICATION_STATUS_NO_LEIDA)
            Log.d(TAG, "Encontradas ${notificacionesNoLeidas.size} notificaciones no leídas")

            // Verificar si hay notificaciones nuevas (comparar con timestamp guardado)
            val notificacionesNuevas = obtenerNotificacionesNuevas(notificacionesNoLeidas)
            
            if (notificacionesNuevas.isNotEmpty()) {
                Log.d(TAG, "Encontradas ${notificacionesNuevas.size} notificaciones nuevas")
                
                // Mostrar notificación para cada nueva notificación
                notificacionesNuevas.forEach { notificacion ->
                    notificationHelper.mostrarNotificacion(notificacion)
                }
                
                // Actualizar timestamp de última verificación
                actualizarTimestampVerificacion()
            } else {
                Log.d(TAG, "No hay notificaciones nuevas")
            }

            Result.success()
            
        } catch (e: Exception) {
            Log.e(TAG, "Error en verificación de notificaciones: ${e.message}", e)
            Result.retry()
        }
    }

    /**
     * Filtra las notificaciones que son realmente nuevas
     * comparando con el timestamp de la última verificación
     */
    private suspend fun obtenerNotificacionesNuevas(
        notificaciones: List<Notificacion>
    ): List<Notificacion> {
        val timestampUltimaVerificacion = obtenerTimestampUltimaVerificacion()
        
        return notificaciones.filter { notificacion ->
            // Si no hay timestamp guardado, todas son nuevas
            if (timestampUltimaVerificacion == 0L) {
                true
            } else {
                // Comparar fecha de notificación con timestamp guardado
                val fechaNotificacion = parsearFechaNotificacion(notificacion.fecha_notificacion)
                fechaNotificacion > timestampUltimaVerificacion
            }
        }
    }

    /**
     * Parsea la fecha de notificación al formato timestamp
     */
    private fun parsearFechaNotificacion(fechaString: String): Long {
        return try {
            // Formato esperado: "Wed, 30 Jul 2025 11:42:14 GMT"
            val formatter = java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
            val zonedDateTime = java.time.ZonedDateTime.parse(fechaString, formatter)
            zonedDateTime.toInstant().toEpochMilli()
        } catch (e: Exception) {
            Log.e(TAG, "Error parseando fecha: $fechaString", e)
            System.currentTimeMillis() // Fallback a tiempo actual
        }
    }

    /**
     * Obtiene el timestamp de la última verificación desde SharedPreferences
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
    }
}
