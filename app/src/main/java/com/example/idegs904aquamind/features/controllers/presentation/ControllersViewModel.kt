package com.example.idegs904aquamind.features.controllers.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.features.controllers.data.ControllersRepository
import com.example.idegs904aquamind.features.controllers.domain.GetNodosUseCase
import com.example.idegs904aquamind.features.controllers.domain.ControlDeviceUseCase
import com.example.idegs904aquamind.features.controllers.service.ControllersUpdateScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ControllersUiState {
    object Loading : ControllersUiState()
    data class Success(val nodos: List<Nodo>) : ControllersUiState()
    data class Error(val message: String) : ControllersUiState()
}

data class NodoWithLoadingState(
    val nodo: Nodo,
    val isLoading: Boolean = false
)

class ControllersViewModel(context: Context) : ViewModel() {
    private val repository = ControllersRepository(context)
    private val getNodosUseCase = GetNodosUseCase(repository)
    private val controlDeviceUseCase = ControlDeviceUseCase(repository)
    private val controllersScheduler = ControllersUpdateScheduler(context)

    private val _uiState = MutableStateFlow<ControllersUiState>(ControllersUiState.Loading)
    val uiState: StateFlow<ControllersUiState> = _uiState
    
    private val _nodosWithLoading = MutableStateFlow<List<NodoWithLoadingState>>(emptyList())
    val nodosWithLoading: StateFlow<List<NodoWithLoadingState>> = _nodosWithLoading

    fun loadNodos() {
        viewModelScope.launch {
            try {
                val nodos = getNodosUseCase()
                _uiState.value = ControllersUiState.Success(nodos)
                _nodosWithLoading.value = nodos.map { NodoWithLoadingState(it) }
                
                // Iniciar actualizaciones automáticas
                iniciarActualizacionesAutomaticas()
            } catch (e: Exception) {
                _uiState.value = ControllersUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
    
    private fun iniciarActualizacionesAutomaticas() {
        controllersScheduler.iniciarActualizacionesPeriodicas { nodosActualizados ->
            viewModelScope.launch {
                try {
                    _uiState.value = ControllersUiState.Success(nodosActualizados)
                    _nodosWithLoading.value = nodosActualizados.map { NodoWithLoadingState(it) }
                } catch (e: Exception) {
                    // No cambiar el estado si hay error en la actualización automática
                    // Solo log del error
                }
            }
        }
    }
    
    fun toggleNodoEstatus(nodo: Nodo) {
        val newStatus = if (nodo.id_estatus == 1) 0 else 1
        
        viewModelScope.launch {
            // Optimistic update
            updateNodoLoadingState(nodo.id_nodo, true)
            
            // Real API call
            val result = controlDeviceUseCase(nodo, newStatus)
            if (result.isSuccess) {
                // Confirmar cambio exitoso
                updateNodoStatus(nodo.id_nodo, newStatus)
                updateNodoLoadingState(nodo.id_nodo, false)
            } else {
                // Revertir cambio y mostrar error
                updateNodoStatus(nodo.id_nodo, nodo.id_estatus)
                updateNodoLoadingState(nodo.id_nodo, false)
                
                val exception = result.exceptionOrNull()
                val errorMessage = when {
                    exception is IllegalArgumentException -> 
                        "Dispositivo no compatible: ${nodo.descripcion}"
                    else -> 
                        "Error al controlar dispositivo: ${exception?.message}"
                }
                _uiState.value = ControllersUiState.Error(errorMessage)
            }
        }
    }
    
    private fun updateNodoStatus(nodoId: Int, newStatus: Int) {
        val currentNodos = _nodosWithLoading.value.toMutableList()
        val index = currentNodos.indexOfFirst { it.nodo.id_nodo == nodoId }
        
        if (index != -1) {
            val updatedNodo = currentNodos[index].nodo.copy(id_estatus = newStatus)
            currentNodos[index] = currentNodos[index].copy(nodo = updatedNodo)
            _nodosWithLoading.value = currentNodos
            
            // También actualizar el estado principal
            val currentSuccessState = _uiState.value as? ControllersUiState.Success
            if (currentSuccessState != null) {
                val updatedNodos = currentSuccessState.nodos.map { 
                    if (it.id_nodo == nodoId) it.copy(id_estatus = newStatus) else it 
                }
                _uiState.value = ControllersUiState.Success(updatedNodos)
            }
        }
    }
    
    private fun updateNodoLoadingState(nodoId: Int, isLoading: Boolean) {
        val currentNodos = _nodosWithLoading.value.toMutableList()
        val index = currentNodos.indexOfFirst { it.nodo.id_nodo == nodoId }
        
        if (index != -1) {
            currentNodos[index] = currentNodos[index].copy(isLoading = isLoading)
            _nodosWithLoading.value = currentNodos
        }
    }
    
    /**
     * Detiene las actualizaciones automáticas
     */
    fun detenerActualizacionesAutomaticas() {
        controllersScheduler.detenerActualizacionesPeriodicas()
    }
    
    /**
     * Ejecuta una actualización inmediata
     */
    fun actualizarInmediatamente() {
        controllersScheduler.ejecutarActualizacionInmediata()
    }
} 