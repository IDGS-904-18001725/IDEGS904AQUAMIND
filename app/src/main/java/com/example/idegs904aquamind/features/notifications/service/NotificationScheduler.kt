package com.example.idegs904aquamind.features.notifications.service

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Scheduler para programar verificaciones periódicas de notificaciones.
 * Configurado para demostración con verificación cada 30 segundos.
 */
class NotificationScheduler(private val context: Context) {

    companion object {
        private const val TAG = "NotificationScheduler"
        private const val WORK_NAME = "notification_check_work"
        private const val FREQUENCY_SECONDS = 30L // Para demostración
    }

    private val workManager = WorkManager.getInstance(context)

    /**
     * Inicia las verificaciones periódicas de notificaciones
     */
    fun iniciarVerificacionesPeriodicas() {
        try {
            Log.d(TAG, "Iniciando verificaciones periódicas cada $FREQUENCY_SECONDS segundos")

            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                FREQUENCY_SECONDS, TimeUnit.SECONDS
            ).build()

            workManager.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )

            Log.d(TAG, "Verificaciones periódicas programadas exitosamente")
            
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
            
            workManager.cancelUniqueWork(WORK_NAME)
            
            Log.d(TAG, "Verificaciones periódicas detenidas")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error deteniendo verificaciones periódicas: ${e.message}", e)
        }
    }

    /**
     * Verifica si las verificaciones están activas
     */
    fun estanVerificacionesActivas(): Boolean {
        return try {
            val workInfo = workManager.getWorkInfosForUniqueWork(WORK_NAME)
            val workInfoList = workInfo.get()
            
            workInfoList.any { it.state.isFinished.not() }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error verificando estado de verificaciones: ${e.message}", e)
            false
        }
    }

    /**
     * Obtiene información sobre el estado actual de las verificaciones
     */
    fun obtenerEstadoVerificaciones(): String {
        return try {
            val workInfo = workManager.getWorkInfosForUniqueWork(WORK_NAME)
            val workInfoList = workInfo.get()
            
            if (workInfoList.isEmpty()) {
                "No hay verificaciones programadas"
            } else {
                val estado = workInfoList.first().state
                "Estado: $estado, Frecuencia: $FREQUENCY_SECONDS segundos"
            }
            
        } catch (e: Exception) {
            "Error obteniendo estado: ${e.message}"
        }
    }

    /**
     * Ejecuta una verificación inmediata (para pruebas)
     */
    fun ejecutarVerificacionInmediata() {
        try {
            Log.d(TAG, "Ejecutando verificación inmediata")
            
            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                1, TimeUnit.MINUTES // Una sola vez
            ).build()

            workManager.enqueue(workRequest)
            
            Log.d(TAG, "Verificación inmediata programada")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error ejecutando verificación inmediata: ${e.message}", e)
        }
    }
}
