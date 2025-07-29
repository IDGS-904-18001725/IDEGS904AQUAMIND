package com.example.idegs904aquamind.auth.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.idegs904aquamind.auth.data.AuthRepository
import com.example.idegs904aquamind.auth.domain.LoginUseCase
import com.example.idegs904aquamind.data.model.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estados posibles de la UI durante el flujo de login.
 */
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val response: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

/**
 * ViewModel manual para el flujo de autenticación.
 *
 * @param context Contexto de la aplicación para instanciar AuthRepository.
 */
class LoginViewModel(context: Context) : ViewModel() {

    // Crea repositorio y caso de uso manualmente
    private val authRepository = AuthRepository(context)
    private val loginUseCase   = LoginUseCase(authRepository)

    // Estado interno mutable
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    // Estado expuesto a la UI
    val uiState: StateFlow<LoginUiState> = _uiState

    /**
     * Inicia el login con las credenciales dadas.
     * Actualiza el estado de la UI según el resultado.
     */
    fun doLogin(username: String, password: String) {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                // Ejecuta login y persiste token
                val result: LoginResponse = loginUseCase(username, password)
                _uiState.value = LoginUiState.Success(result)
            } catch (e: Exception) {
                // Mapea excepción a mensaje de error
                _uiState.value = LoginUiState.Error(
                    e.localizedMessage ?: "Error desconocido"
                )
            }
        }
    }
}
