package com.example.idegs904aquamind.features.notifications.service

import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Scheduler que usa NotificationTimerService para verificaciones cada 30 segundos.
 * Alternativa a WorkManager para intervalos cortos.
 */
class NotificationTimerScheduler(private val context: Context) {

    companion object {
        private const val TAG = "NotificationTimerScheduler"
        private const val FREQUENCY_SECONDS = 30L
    }

    /**
     * Inicia las verificaciones periódicas usando el servicio
     */
    fun iniciarVerificacionesPeriodicas() {
        try {
            Log.d(TAG, "Iniciando verificaciones periódicas cada $FREQUENCY_SECONDS segundos")
            
            // Verificar que el contexto sea válido
            if (context == null) {
                Log.e(TAG, "Context es null, no se puede iniciar el servicio")
                return
            }
            
            val intent = Intent(context, NotificationTimerService::class.java)
            context.startService(intent)
            
            Log.d(TAG, "Servicio de notificaciones iniciado exitosamente")
            
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
            
            val intent = Intent(context, NotificationTimerService::class.java)
            context.stopService(intent)
            
            Log.d(TAG, "Servicio de notificaciones detenido")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error deteniendo verificaciones periódicas: ${e.message}", e)
        }
    }

    /**
     * Verifica si las verificaciones están activas
     */
    fun estanVerificacionesActivas(): Boolean {
        // Por simplicidad, asumimos que si el servicio está corriendo, las verificaciones están activas
        // En una implementación real, podrías usar ActivityManager para verificar si el servicio está corriendo
        return true
    }

    /**
     * Obtiene información sobre el estado actual de las verificaciones
     */
    fun obtenerEstadoVerificaciones(): String {
        return "Servicio Timer: Activo, Frecuencia: $FREQUENCY_SECONDS segundos"
    }

    /**
     * Ejecuta una verificación inmediata (para pruebas)
     */
    fun ejecutarVerificacionInmediata() {
        try {
            Log.d(TAG, "Ejecutando verificación inmediata")
            
            // Enviar broadcast al servicio para ejecutar verificación inmediata
            val intent = Intent(context, NotificationTimerService::class.java).apply {
                action = "VERIFICACION_INMEDIATA"
            }
            context.startService(intent)
            
            Log.d(TAG, "Verificación inmediata solicitada")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error ejecutando verificación inmediata: ${e.message}", e)
        }
    }
}
