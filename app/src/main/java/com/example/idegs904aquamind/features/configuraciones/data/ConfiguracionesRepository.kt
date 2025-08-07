package com.example.idegs904aquamind.features.configuraciones.data

import com.example.idegs904aquamind.data.model.Configuracion
import com.example.idegs904aquamind.data.model.ActualizarConfiguracionRequest
import com.example.idegs904aquamind.network.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para manejar las operaciones de configuraciones del sistema.
 */
class ConfiguracionesRepository(
    private val apiService: ApiService
) {
    
    /**
     * Obtiene todas las configuraciones del sistema.
     */
    suspend fun getConfiguraciones(): List<Configuracion> = withContext(Dispatchers.IO) {
        try {
            apiService.getConfiguraciones()
        } catch (e: Exception) {
            throw e
        }
    }
    
    /**
     * Actualiza una configuración específica.
     */
    suspend fun actualizarConfiguracion(
        id: Int,
        valor: String
    ): Configuracion = withContext(Dispatchers.IO) {
        try {
            val request = ActualizarConfiguracionRequest(valor)
            apiService.actualizarConfiguracion(id, request)
        } catch (e: Exception) {
            throw e
        }
    }
}
