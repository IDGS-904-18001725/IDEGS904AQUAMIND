package com.example.idegs904aquamind.auth.presentation
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.LoginResponse

/**
 * Pantalla de login con validaciones básicas:
 * - Deshabilita botón si campos vacíos.
 * - Muestra mensajes de error de validación.
 * - Maneja estados de carga y error de autenticación.
 *
 * @param onLoginSuccess Callback invocado con el LoginResponse en caso de éxito.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: (LoginResponse) -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { LoginViewModel(context) }
    val uiState by viewModel.uiState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de usuario con validación
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                if (it.isNotBlank()) usernameError = null
            },
            label = { Text("Usuario") },
            isError = usernameError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (usernameError != null) {
            Text(
                text = usernameError!!,
                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }

        // Campo de contraseña con validación
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                if (it.isNotBlank()) passwordError = null
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }

        // Botón de login
        Button(
            onClick = {
                var valid = true
                if (username.isBlank()) {
                    usernameError = "El usuario no puede estar vacío"
                    valid = false
                }
                if (password.isBlank()) {
                    passwordError = "La contraseña no puede estar vacía"
                    valid = false
                }
                if (valid) viewModel.doLogin(username, password)
            },
            enabled = username.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        // Estado de carga
        if (uiState is LoginUiState.Loading) {
            CircularProgressIndicator()
        }
        // Error de autenticación
        if (uiState is LoginUiState.Error) {
            Text(
                text = (uiState as LoginUiState.Error).message,
                color = androidx.compose.material3.MaterialTheme.colorScheme.error
            )
        }
        // Éxito de login
        if (uiState is LoginUiState.Success) {
            LaunchedEffect(uiState) {
                onLoginSuccess((uiState as LoginUiState.Success).response)
            }
        }
    }
}
