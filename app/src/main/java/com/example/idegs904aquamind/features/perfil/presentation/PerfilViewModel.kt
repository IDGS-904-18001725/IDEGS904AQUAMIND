package com.example.idegs904aquamind.features.perfil.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.features.perfil.data.PerfilRepository
import com.example.idegs904aquamind.features.perfil.data.model.Usuario
import com.example.idegs904aquamind.features.perfil.data.model.ActualizarUsuarioRequest
import com.example.idegs904aquamind.features.perfil.domain.ObtenerUsuarioUseCase
import com.example.idegs904aquamind.features.perfil.domain.ActualizarUsuarioUseCase
import com.example.idegs904aquamind.features.perfil.domain.ActualizarImagenPerfilUseCase
import com.example.idegs904aquamind.network.service.RetrofitBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para el módulo de perfil de usuario
 */
class PerfilViewModel(
    private val context: Context
) : ViewModel() {
    
    // Dependencias
    private val repository = PerfilRepository(RetrofitBuilder.getApiService(context))
    private val obtenerUsuarioUseCase = ObtenerUsuarioUseCase(repository)
    private val actualizarUsuarioUseCase = ActualizarUsuarioUseCase(repository)
    private val actualizarImagenPerfilUseCase = ActualizarImagenPerfilUseCase(repository)
    private val sessionManager = SessionManager(context)
    
    // Estado de la UI
    private val _uiState = MutableStateFlow<PerfilUiState>(PerfilUiState.Loading)
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()
    
    // Datos del usuario actual
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()
    
    // Eventos de navegación
    private val _navigationEvent = MutableStateFlow<PerfilNavigationEvent?>(null)
    val navigationEvent: StateFlow<PerfilNavigationEvent?> = _navigationEvent.asStateFlow()
    
    // ID del usuario actual (se obtiene del SessionManager)
    private var idUsuarioActual: Int = 0
    
    init {
        cargarUsuario()
    }
    
    /**
     * Carga la información del usuario actual
     */
    private fun cargarUsuario() {
        viewModelScope.launch {
            try {
                _uiState.value = PerfilUiState.Loading
                
                // Obtener ID del usuario desde SessionManager
                val user = sessionManager.getUser()
                if (user != null) {
                    idUsuarioActual = user.id
                    try {
                        val usuario = obtenerUsuarioUseCase(idUsuarioActual)
                        _usuario.value = usuario
                        _uiState.value = PerfilUiState.Success
                    } catch (apiException: Exception) {
                        _uiState.value = PerfilUiState.Error("Error de API: ${apiException.message}")
                    }
                } else {
                    _uiState.value = PerfilUiState.Error("No se encontró información del usuario. Por favor, inicie sesión nuevamente.")
                }
                
            } catch (e: Exception) {
                _uiState.value = PerfilUiState.Error("Error general: ${e.message}")
            }
        }
    }
    
    /**
     * Actualiza la información del usuario
     */
    fun actualizarUsuario(request: ActualizarUsuarioRequest) {
        viewModelScope.launch {
            try {
                _uiState.value = PerfilUiState.Loading
                
                val usuarioActualizado = actualizarUsuarioUseCase(idUsuarioActual, request)
                _usuario.value = usuarioActualizado
                _uiState.value = PerfilUiState.Success
                
            } catch (e: Exception) {
                _uiState.value = PerfilUiState.Error(e.message ?: "Error al actualizar el perfil")
            }
        }
    }
    
    /**
     * Actualiza solo la imagen de perfil
     */
    fun actualizarImagenPerfil(imagenBase64: String) {
        viewModelScope.launch {
            try {
                _uiState.value = PerfilUiState.Loading
                
                val usuarioActualizado = actualizarImagenPerfilUseCase(idUsuarioActual, imagenBase64)
                _usuario.value = usuarioActualizado
                _uiState.value = PerfilUiState.Success
                
            } catch (e: Exception) {
                _uiState.value = PerfilUiState.Error(e.message ?: "Error al actualizar la imagen")
            }
        }
    }
    
    /**
     * Cierra la sesión del usuario
     */
    fun cerrarSesion() {
        viewModelScope.launch {
            try {
                _uiState.value = PerfilUiState.Loading
                
                // Limpiar sesión
                sessionManager.clearSession()
                
                // Limpiar datos del usuario
                _usuario.value = null
                idUsuarioActual = 0
                
                _uiState.value = PerfilUiState.CerrandoSesion
                
                // Emitir evento de navegación
                _navigationEvent.value = PerfilNavigationEvent.NavigateToLogin
                
            } catch (e: Exception) {
                _uiState.value = PerfilUiState.Error(e.message ?: "Error al cerrar sesión")
            }
        }
    }
    
    /**
     * Recarga la información del usuario
     */
    fun recargarUsuario() {
        cargarUsuario()
    }
    
    /**
     * Limpia el evento de navegación
     */
    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}

/**
 * Estados de la UI para el perfil
 */
sealed class PerfilUiState {
    object Loading : PerfilUiState()
    object Success : PerfilUiState()
    object CerrandoSesion : PerfilUiState()
    data class Error(val message: String) : PerfilUiState()
} 