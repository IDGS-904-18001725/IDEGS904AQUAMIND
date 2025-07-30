package com.example.idegs904aquamind.features.dashboard.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.data.model.Evento
import com.example.idegs904aquamind.data.model.TipoPeriodo
import com.example.idegs904aquamind.features.eventos.data.EventosRepository
import com.example.idegs904aquamind.features.eventos.domain.GetEventosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(
        val nombreUsuario: String,
        val eventos: List<Evento>
    ) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
    object Empty : DashboardUiState()
}

class DashboardViewModel(context: Context) : ViewModel() {
    private val sessionManager = SessionManager(context)
    private val repository = EventosRepository(context)
    private val getEventosUseCase = GetEventosUseCase(repository)

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Empty)
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun cargarDashboard() {
        _uiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            try {
                // Obtener nombre del usuario
                val nombreUsuario = sessionManager.getUserFullName() ?: "Usuario"
                
                // Obtener datos de consumo de los últimos 7 días
                val eventos = getEventosUseCase.getEventosPorPeriodo(7, TipoPeriodo.DIAS.id)
                
                _uiState.value = DashboardUiState.Success(nombreUsuario, eventos)
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("consumo_total") == true -> "No hay datos de consumo disponibles"
                    e.message?.contains("No se encontraron eventos") == true -> "No se encontraron eventos en el período seleccionado"
                    e.message?.contains("401") == true -> "Sesión expirada, por favor inicia sesión nuevamente"
                    e.message?.contains("404") == true -> "Servicio no disponible"
                    e.message?.contains("500") == true -> "Error del servidor, intenta más tarde"
                    else -> "Error de conexión, verifica tu internet e intenta nuevamente"
                }
                _uiState.value = DashboardUiState.Error(errorMessage)
            }
        }
    }
} 