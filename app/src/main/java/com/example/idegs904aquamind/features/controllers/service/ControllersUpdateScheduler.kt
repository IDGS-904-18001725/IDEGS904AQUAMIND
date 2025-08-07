package com.example.idegs904aquamind.features.controllers.service

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.features.controllers.data.ControllersRepository

/**
 * Scheduler que actualiza los controles cada cierto tiempo.
 * Mantiene la vista de controles actualizada automáticamente.
 */
class ControllersUpdateScheduler(private val context: Context) {

    companion object {
        private const val TAG = "ControllersUpdateScheduler"
        private const val FREQUENCY_SECONDS = 45L // Actualizar cada 45 segundos
    }

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val repository = ControllersRepository(context)
    private val sessionManager = SessionManager(context)

    // Callback para notificar cuando hay nuevos datos
    private var onDataUpdated: ((List<com.example.idegs904aquamind.data.model.Nodo>) -> Unit)? = null

    /**
     * Inicia las actualizaciones periódicas de controles
     */
    fun iniciarActualizacionesPeriodicas(
        onDataUpdated: (List<com.example.idegs904aquamind.data.model.Nodo>) -> Unit
    ) {
        try {
            Log.d(TAG, "Iniciando actualizaciones de controles cada $FREQUENCY_SECONDS segundos")
            
            this.onDataUpdated = onDataUpdated
            
            // Cancelar job existente si hay uno
            job?.cancel()
            
            // Crear nuevo job
            job = scope.launch {
                while (isActive) {
                    try {
                        actualizarControles()
                        delay(FREQUENCY_SECONDS * 1000) // Esperar 45 segundos
                    } catch (e: Exception) {
                        Log.e(TAG, "Error en actualización de controles: ${e.message}", e)
                        delay(10000) // Esperar 10 segundos antes de reintentar
                    }
                }
            }
            
            Log.d(TAG, "Actualizaciones de controles iniciadas exitosamente")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error iniciando actualizaciones de controles: ${e.message}", e)
        }
    }

    /**
     * Detiene las actualizaciones periódicas
     */
    fun detenerActualizacionesPeriodicas() {
        try {
            Log.d(TAG, "Deteniendo actualizaciones de controles")
            
            job?.cancel()
            job = null
            onDataUpdated = null
            
            Log.d(TAG, "Actualizaciones de controles detenidas")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error deteniendo actualizaciones de controles: ${e.message}", e)
        }
    }

    /**
     * Verifica si las actualizaciones están activas
     */
    fun estanActualizacionesActivas(): Boolean {
        return job?.isActive == true
    }

    /**
     * Obtiene información sobre el estado actual de las actualizaciones
     */
    fun obtenerEstadoActualizaciones(): String {
        return if (estanActualizacionesActivas()) {
            "Actualizaciones Activas, Frecuencia: $FREQUENCY_SECONDS segundos"
        } else {
            "Actualizaciones Inactivas"
        }
    }

    /**
     * Ejecuta una actualización inmediata (para pruebas)
     */
    fun ejecutarActualizacionInmediata() {
        try {
            Log.d(TAG, "Ejecutando actualización inmediata de controles")
            
            scope.launch {
                actualizarControles()
            }
            
            Log.d(TAG, "Actualización inmediata ejecutada")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error ejecutando actualización inmediata: ${e.message}", e)
        }
    }

    /**
     * Actualiza los controles obteniendo datos del servidor
     */
    private suspend fun actualizarControles() {
        try {
            Log.d(TAG, "=== ACTUALIZANDO CONTROLES ===")
            
            // Verificar si hay sesión activa
            val token = sessionManager.getToken()
            if (token.isNullOrEmpty()) {
                Log.d(TAG, "No hay sesión activa, saltando actualización de controles")
                return
            }

            Log.d(TAG, "Sesión activa encontrada, actualizando controles...")

            // Obtener nodos/controles del servidor
            val nodos = repository.getNodos()
            Log.d(TAG, "Obtenidos ${nodos.size} nodos/controles del servidor")

            // Notificar a la UI que hay nuevos datos
            onDataUpdated?.invoke(nodos)
            
            Log.d(TAG, "Controles actualizados exitosamente")
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en actualización de controles: ${e.message}", e)
        }
    }
}
