package com.example.idegs904aquamind.features.controllers.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.features.controllers.data.ControllersRepository
import com.example.idegs904aquamind.features.controllers.domain.GetNodosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ControllersUiState {
    object Loading : ControllersUiState()
    data class Success(val nodos: List<Nodo>) : ControllersUiState()
    data class Error(val message: String) : ControllersUiState()
}

class ControllersViewModel(context: Context) : ViewModel() {
    private val repository = ControllersRepository(context)
    private val getNodosUseCase = GetNodosUseCase(repository)

    private val _uiState = MutableStateFlow<ControllersUiState>(ControllersUiState.Loading)
    val uiState: StateFlow<ControllersUiState> = _uiState

    fun loadNodos() {
        viewModelScope.launch {
            try {
                val nodos = getNodosUseCase()
                _uiState.value = ControllersUiState.Success(nodos)
            } catch (e: Exception) {
                _uiState.value = ControllersUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
} 