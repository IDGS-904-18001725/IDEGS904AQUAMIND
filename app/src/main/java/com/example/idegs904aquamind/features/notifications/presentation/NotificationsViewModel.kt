package com.example.idegs904aquamind.features.notifications.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.features.notifications.data.NotificacionesRepository
import com.example.idegs904aquamind.features.notifications.domain.GetNotificacionesUseCase
import com.example.idegs904aquamind.features.notifications.domain.MarcarNotificacionLeidaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NotificacionesUiState {
    object Loading : NotificacionesUiState()
    data class Success(val notificaciones: List<Notificacion>) : NotificacionesUiState()
    data class Error(val message: String) : NotificacionesUiState()
    object Empty : NotificacionesUiState()
}

class NotificationsViewModel(context: Context) : ViewModel() {
    private val repository = NotificacionesRepository(context)
    private val getNotificacionesUseCase = GetNotificacionesUseCase(repository)
    private val marcarLeidaUseCase = MarcarNotificacionLeidaUseCase(repository)

    private val _uiState = MutableStateFlow<NotificacionesUiState>(NotificacionesUiState.Empty)
    val uiState: StateFlow<NotificacionesUiState> = _uiState

    fun cargarNotificaciones() {
        _uiState.value = NotificacionesUiState.Loading
        viewModelScope.launch {
            try {
                val notificaciones = getNotificacionesUseCase()
                
                if (notificaciones.isEmpty()) {
                    _uiState.value = NotificacionesUiState.Empty
                } else {
                    _uiState.value = NotificacionesUiState.Success(notificaciones)
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true -> "Sesión expirada, por favor inicia sesión nuevamente"
                    e.message?.contains("404") == true -> "Servicio no disponible"
                    e.message?.contains("500") == true -> "Error del servidor, intenta más tarde"
                    else -> "Error de conexión, verifica tu internet e intenta nuevamente"
                }
                _uiState.value = NotificacionesUiState.Error(errorMessage)
            }
        }
    }

    fun marcarComoLeida(notificacion: Notificacion) {
        // Solo marcar como leída si no está leída
        if (notificacion.id_estatus != 2) {
            viewModelScope.launch {
                try {
                    marcarLeidaUseCase(notificacion.id_notificacion)
                    // Recargar notificaciones para actualizar la UI
                    cargarNotificaciones()
                } catch (e: Exception) {
                    // Manejar error silenciosamente o mostrar mensaje
                    val errorMessage = "Error al marcar como leída: ${e.localizedMessage}"
                    _uiState.value = NotificacionesUiState.Error(errorMessage)
                }
            }
        }
    }
} 