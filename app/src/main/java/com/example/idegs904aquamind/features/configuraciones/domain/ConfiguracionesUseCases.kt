package com.example.idegs904aquamind.features.configuraciones.domain

import com.example.idegs904aquamind.data.model.Configuracion
import com.example.idegs904aquamind.features.configuraciones.data.ConfiguracionesRepository

/**
 * Caso de uso para obtener todas las configuraciones del sistema.
 */
class GetConfiguracionesUseCase(
    private val repository: ConfiguracionesRepository
) {
    suspend operator fun invoke(): List<Configuracion> {
        return repository.getConfiguraciones()
    }
}

/**
 * Caso de uso para actualizar una configuración específica.
 */
class ActualizarConfiguracionUseCase(
    private val repository: ConfiguracionesRepository
) {
    suspend operator fun invoke(id: Int, valor: String): Configuracion {
        return repository.actualizarConfiguracion(id, valor)
    }
}
