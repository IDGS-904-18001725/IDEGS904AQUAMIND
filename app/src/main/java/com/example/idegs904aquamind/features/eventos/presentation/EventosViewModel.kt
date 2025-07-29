package com.example.idegs904aquamind.features.eventos.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.data.model.Evento
import com.example.idegs904aquamind.data.model.TipoConsulta
import com.example.idegs904aquamind.data.model.TipoPeriodo
import com.example.idegs904aquamind.features.eventos.data.EventosRepository
import com.example.idegs904aquamind.features.eventos.domain.GetEventosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class EventosUiState {
    object Loading : EventosUiState()
    data class Success(val eventos: List<Evento>, val tipoConsulta: TipoConsulta) : EventosUiState()
    data class Error(val message: String) : EventosUiState()
    object Empty : EventosUiState()
}

class EventosViewModel(context: Context) : ViewModel() {
    private val repository = EventosRepository(context)
    private val getEventosUseCase = GetEventosUseCase(repository)

    private val _uiState = MutableStateFlow<EventosUiState>(EventosUiState.Empty)
    val uiState: StateFlow<EventosUiState> = _uiState

    // Estado de la UI
    private val _tipoConsulta = MutableStateFlow(TipoConsulta.INTERVALO)
    val tipoConsulta: StateFlow<TipoConsulta> = _tipoConsulta

    private val _fechaInicio = MutableStateFlow(LocalDate.now().minusDays(7).format(DateTimeFormatter.ISO_DATE))
    val fechaInicio: StateFlow<String> = _fechaInicio

    private val _fechaFin = MutableStateFlow(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
    val fechaFin: StateFlow<String> = _fechaFin

    private val _cantidadPeriodos = MutableStateFlow(7)
    val cantidadPeriodos: StateFlow<Int> = _cantidadPeriodos

    private val _tipoPeriodo = MutableStateFlow(TipoPeriodo.DIAS)
    val tipoPeriodo: StateFlow<TipoPeriodo> = _tipoPeriodo

    fun setTipoConsulta(tipo: TipoConsulta) {
        _tipoConsulta.value = tipo
    }

    fun setFechas(inicio: String, fin: String) {
        _fechaInicio.value = inicio
        _fechaFin.value = fin
    }

    fun setPeriodo(cantidad: Int, tipo: TipoPeriodo) {
        _cantidadPeriodos.value = cantidad
        _tipoPeriodo.value = tipo
    }

    fun buscarEventos() {
        _uiState.value = EventosUiState.Loading
        viewModelScope.launch {
            try {
                val eventos = when (_tipoConsulta.value) {
                    TipoConsulta.INTERVALO -> {
                        getEventosUseCase.getEventosPorFechas(_fechaInicio.value, _fechaFin.value)
                    }
                    TipoConsulta.PERIODO -> {
                        getEventosUseCase.getEventosPorPeriodo(_cantidadPeriodos.value, _tipoPeriodo.value.id)
                    }
                }
                
                if (eventos.isEmpty()) {
                    _uiState.value = EventosUiState.Empty
                } else {
                    _uiState.value = EventosUiState.Success(eventos, _tipoConsulta.value)
                }
            } catch (e: Exception) {
                // Manejar errores específicos de la API
                val errorMessage = when {
                    e.message?.contains("consumo_total") == true -> "No hay datos de consumo disponibles"
                    e.message?.contains("No se encontraron eventos") == true -> "No se encontraron eventos en el período seleccionado"
                    e.message?.contains("401") == true -> "Sesión expirada, por favor inicia sesión nuevamente"
                    e.message?.contains("404") == true -> "Servicio no disponible"
                    e.message?.contains("500") == true -> "Error del servidor, intenta más tarde"
                    else -> "Error de conexión, verifica tu internet e intenta nuevamente"
                }
                _uiState.value = EventosUiState.Error(errorMessage)
            }
        }
    }
} 