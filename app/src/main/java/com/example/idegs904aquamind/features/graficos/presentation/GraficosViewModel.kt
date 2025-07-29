package com.example.idegs904aquamind.features.graficos.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.data.model.Evento
import com.example.idegs904aquamind.data.model.TipoPeriodo
import com.example.idegs904aquamind.features.eventos.data.EventosRepository
import com.example.idegs904aquamind.features.eventos.domain.GetEventosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class GraficosUiState {
    object Loading : GraficosUiState()
    data class Success(val eventos: List<Evento>) : GraficosUiState()
    data class Error(val message: String) : GraficosUiState()
    object Empty : GraficosUiState()
}

class GraficosViewModel(context: Context) : ViewModel() {
    private val repository = EventosRepository(context)
    private val getEventosUseCase = GetEventosUseCase(repository)

    private val _uiState = MutableStateFlow<GraficosUiState>(GraficosUiState.Empty)
    val uiState: StateFlow<GraficosUiState> = _uiState

    // Estado de la UI - solo para períodos
    private val _cantidadPeriodos = MutableStateFlow(30) // Por defecto 30 días
    val cantidadPeriodos: StateFlow<Int> = _cantidadPeriodos

    private val _tipoPeriodo = MutableStateFlow(TipoPeriodo.DIAS) // Por defecto días
    val tipoPeriodo: StateFlow<TipoPeriodo> = _tipoPeriodo

    fun setPeriodo(cantidad: Int, tipo: TipoPeriodo) {
        _cantidadPeriodos.value = cantidad
        _tipoPeriodo.value = tipo
    }

    fun cargarDatosGraficos() {
        _uiState.value = GraficosUiState.Loading
        viewModelScope.launch {
            try {
                val eventos = getEventosUseCase.getEventosPorPeriodo(
                    _cantidadPeriodos.value, 
                    _tipoPeriodo.value.id
                )
                
                if (eventos.isEmpty()) {
                    _uiState.value = GraficosUiState.Empty
                } else {
                    _uiState.value = GraficosUiState.Success(eventos)
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("consumo_total") == true -> "No hay datos de consumo disponibles"
                    e.message?.contains("No se encontraron eventos") == true -> "No se encontraron eventos en el período seleccionado"
                    e.message?.contains("401") == true -> "Sesión expirada, por favor inicia sesión nuevamente"
                    e.message?.contains("404") == true -> "Servicio no disponible"
                    e.message?.contains("500") == true -> "Error del servidor, intenta más tarde"
                    else -> "Error de conexión, verifica tu internet e intenta nuevamente"
                }
                _uiState.value = GraficosUiState.Error(errorMessage)
            }
        }
    }
} 