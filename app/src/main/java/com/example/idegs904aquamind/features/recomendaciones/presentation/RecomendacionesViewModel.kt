package com.example.idegs904aquamind.features.recomendaciones.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.data.model.Recomendacion
import com.example.idegs904aquamind.features.recomendaciones.data.RecomendacionesRepository
import com.example.idegs904aquamind.features.recomendaciones.domain.GetRecomendacionesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class RecomendacionesUiState {
    object Loading : RecomendacionesUiState()
    data class Success(val recomendaciones: List<Recomendacion>) : RecomendacionesUiState()
    data class Error(val message: String) : RecomendacionesUiState()
    object Empty : RecomendacionesUiState()
}

class RecomendacionesViewModel(context: Context) : ViewModel() {
    private val repository = RecomendacionesRepository(context)
    private val getRecomendacionesUseCase = GetRecomendacionesUseCase(repository)

    private val _uiState = MutableStateFlow<RecomendacionesUiState>(RecomendacionesUiState.Loading)
    val uiState: StateFlow<RecomendacionesUiState> = _uiState

    fun loadRecomendaciones() {
        viewModelScope.launch {
            try {
                val recomendaciones = getRecomendacionesUseCase()
                if (recomendaciones.isEmpty()) {
                    _uiState.value = RecomendacionesUiState.Empty
                } else {
                    _uiState.value = RecomendacionesUiState.Success(recomendaciones)
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true -> "Sesión expirada, por favor inicia sesión nuevamente"
                    e.message?.contains("404") == true -> "Servicio no disponible"
                    e.message?.contains("500") == true -> "Error del servidor, intenta más tarde"
                    else -> "Error de conexión, verifica tu internet e intenta nuevamente"
                }
                _uiState.value = RecomendacionesUiState.Error(errorMessage)
            }
        }
    }
} 