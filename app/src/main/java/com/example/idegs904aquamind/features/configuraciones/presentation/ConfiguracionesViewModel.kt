package com.example.idegs904aquamind.features.configuraciones.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.data.model.Configuracion
import com.example.idegs904aquamind.features.configuraciones.domain.GetConfiguracionesUseCase
import com.example.idegs904aquamind.features.configuraciones.domain.ActualizarConfiguracionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de la pantalla de configuraciones.
 */
data class ConfiguracionesState(
    val configuraciones: List<Configuracion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdating: Boolean = false,
    val updateError: String? = null
)

/**
 * ViewModel para manejar la lógica de configuraciones del sistema.
 */
class ConfiguracionesViewModel(
    private val getConfiguracionesUseCase: GetConfiguracionesUseCase,
    private val actualizarConfiguracionUseCase: ActualizarConfiguracionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ConfiguracionesState())
    val state: StateFlow<ConfiguracionesState> = _state.asStateFlow()

    init {
        cargarConfiguraciones()
    }

    /**
     * Carga las configuraciones desde el servidor.
     */
    fun cargarConfiguraciones() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val configuraciones = getConfiguracionesUseCase()
                _state.value = _state.value.copy(
                    configuraciones = configuraciones,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar configuraciones: ${e.message}"
                )
            }
        }
    }

    /**
     * Actualiza una configuración específica.
     */
    fun actualizarConfiguracion(id: Int, valor: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdating = true, updateError = null)
            try {
                val configuracionActualizada = actualizarConfiguracionUseCase(id, valor)
                
                // Actualizar la lista local con la configuración actualizada
                val configuracionesActualizadas = _state.value.configuraciones.map { config ->
                    if (config.id_configuracion == id) {
                        configuracionActualizada
                    } else {
                        config
                    }
                }
                
                _state.value = _state.value.copy(
                    configuraciones = configuracionesActualizadas,
                    isUpdating = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isUpdating = false,
                    updateError = "Error al actualizar configuración: ${e.message}"
                )
            }
        }
    }

    /**
     * Limpia los errores de actualización.
     */
    fun limpiarErrores() {
        _state.value = _state.value.copy(
            error = null,
            updateError = null
        )
    }
}
