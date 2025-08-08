package com.example.idegs904aquamind.features.notifications.service

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.features.notifications.data.NotificacionesRepository
import com.example.idegs904aquamind.features.configuraciones.data.ConfiguracionesLocalManager

/**
 * Scheduler simple que usa coroutines para verificaciones cada 30 segundos.
 * Más seguro que usar servicios para evitar crashes.
 */
class SimpleNotificationScheduler(private val context: Context) {

    companion object {
        private const val TAG = "SimpleNotificationScheduler"
        private const val DEFAULT_FREQUENCY_SECONDS = 30L
    }

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val repository = NotificacionesRepository(context)
    private val sessionManager = SessionManager(context)
    private val notificationHelper = NotificationHelper(context)
    private val configuracionesLocalManager = ConfiguracionesLocalManager(context)

    /**
     * Inicia las verificaciones periódicas usando coroutines
     */
    fun iniciarVerificacionesPeriodicas() {
        try {
            // Obtener frecuencia configurada
            val frecuencia = configuracionesLocalManager.getFrecuenciaNotificaciones().toLong()
            Log.d(TAG, "Iniciando verificaciones periódicas cada $frecuencia segundos")
            
            // Cancelar job existente si hay uno
            job?.cancel()
            
            // Crear nuevo job
            job = scope.launch {
                while (isActive) {
                    try {
                        Log.d(TAG, "Ejecutando verificación de notificaciones")
                        verificarNotificaciones()
                        Log.d(TAG, "Esperando $frecuencia segundos hasta la próxima verificación")
                        delay(frecuencia * 1000) // Esperar según la frecuencia configurada
                    } catch (e: Exception) {
                        Log.e(TAG, "Error en verificación: ${e.message}", e)
                        delay(5000) // Esperar 5 segundos antes de reintentar
                    }
                }
            }
            
            Log.d(TAG, "Verificaciones periódicas iniciadas exitosamente con frecuencia: $frecuencia segundos")
            
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
            val frecuencia = configuracionesLocalManager.getFrecuenciaNotificaciones()
            "Coroutine Activa, Frecuencia: $frecuencia segundos"
        } else {
            "Coroutine Inactiva"
        }
    }

    /**
     * Obtiene la frecuencia actual configurada
     */
    fun obtenerFrecuenciaActual(): Int {
        return configuracionesLocalManager.getFrecuenciaNotificaciones()
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

            // Si hay al menos una notificación no leída, mostrar todas
            if (notificacionesNoLeidas.isNotEmpty()) {
                Log.d(TAG, "🎉 Encontradas ${notificacionesNoLeidas.size} notificaciones no leídas")
                
                // Mostrar notificación para cada notificación no leída
                notificacionesNoLeidas.forEach { notificacion ->
                    notificationHelper.mostrarNotificacion(notificacion)
                    Log.d(TAG, "Notificación mostrada: ${notificacion.notificacion}")
                }
            } else {
                Log.d(TAG, "No hay notificaciones no leídas")
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en verificación de notificaciones: ${e.message}", e)
        }
    }


}
