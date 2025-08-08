package com.example.idegs904aquamind.features.configuraciones.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.data.model.Configuracion
import com.example.idegs904aquamind.features.configuraciones.domain.GetConfiguracionesUseCase
import com.example.idegs904aquamind.features.configuraciones.domain.ActualizarConfiguracionUseCase
import com.example.idegs904aquamind.features.configuraciones.data.ConfiguracionesLocalManager
import com.example.idegs904aquamind.features.notifications.service.SimpleNotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * Estado de la pantalla de configuraciones.
 */
data class ConfiguracionesState(
    val configuraciones: List<Configuracion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdating: Boolean = false,
    val updateError: String? = null,
    val actualizacionesAutomaticas: Boolean = false,
    val frecuenciaNotificaciones: Int = 300
)

/**
 * ViewModel para manejar la lógica de configuraciones del sistema.
 */
class ConfiguracionesViewModel(
    private val getConfiguracionesUseCase: GetConfiguracionesUseCase,
    private val actualizarConfiguracionUseCase: ActualizarConfiguracionUseCase,
    private val configuracionesLocalManager: ConfiguracionesLocalManager,
    private val notificationScheduler: SimpleNotificationScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(ConfiguracionesState())
    val state: StateFlow<ConfiguracionesState> = _state.asStateFlow()

    init {
        cargarConfiguraciones()
        cargarEstadoActualizaciones()
        cargarFrecuenciaNotificaciones()
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

    /**
     * Carga el estado de las actualizaciones automáticas desde el almacenamiento local.
     */
    private fun cargarEstadoActualizaciones() {
        val actualizacionesHabilitadas = configuracionesLocalManager.getActualizacionesAutomaticas()
        _state.value = _state.value.copy(
            actualizacionesAutomaticas = actualizacionesHabilitadas
        )
    }

    /**
     * Carga la frecuencia de notificaciones desde el almacenamiento local.
     */
    private fun cargarFrecuenciaNotificaciones() {
        val frecuencia = configuracionesLocalManager.getFrecuenciaNotificaciones()
        _state.value = _state.value.copy(
            frecuenciaNotificaciones = frecuencia
        )
    }

    /**
     * Cambia el estado de las actualizaciones automáticas.
     */
    fun toggleActualizacionesAutomaticas(enabled: Boolean) {
        viewModelScope.launch {
            try {
                // Actualizar estado local
                configuracionesLocalManager.setActualizacionesAutomaticas(enabled)
                
                // Actualizar estado de la UI
                _state.value = _state.value.copy(
                    actualizacionesAutomaticas = enabled
                )
                
                // Controlar el scheduler de notificaciones
                if (enabled) {
                    notificationScheduler.iniciarVerificacionesPeriodicas()
                } else {
                    notificationScheduler.detenerVerificacionesPeriodicas()
                }
                
            } catch (e: Exception) {
                // En caso de error, revertir el estado
                _state.value = _state.value.copy(
                    actualizacionesAutomaticas = !enabled
                )
            }
        }
    }

    /**
     * Actualiza la frecuencia de notificaciones.
     */
    fun actualizarFrecuenciaNotificaciones(segundos: Int) {
        viewModelScope.launch {
            try {
                // Validar que el valor esté en un rango razonable (5-300 segundos)
                val frecuenciaValidada = segundos.coerceIn(5, 300)
                
                // Actualizar estado local
                configuracionesLocalManager.setFrecuenciaNotificaciones(frecuenciaValidada)
                
                // Actualizar estado de la UI
                _state.value = _state.value.copy(
                    frecuenciaNotificaciones = frecuenciaValidada
                )
                
                // Reiniciar el scheduler con la nueva frecuencia si está activo
                if (_state.value.actualizacionesAutomaticas) {
                    android.util.Log.d("ConfiguracionesViewModel", "Reiniciando scheduler con nueva frecuencia: $frecuenciaValidada segundos")
                    notificationScheduler.detenerVerificacionesPeriodicas()
                    delay(100) // Pequeña pausa para asegurar que se detenga
                    notificationScheduler.iniciarVerificacionesPeriodicas()
                }
                
            } catch (e: Exception) {
                // En caso de error, mantener el valor anterior
                android.util.Log.e("ConfiguracionesViewModel", "Error actualizando frecuencia: ${e.message}", e)
            }
        }
    }
}
