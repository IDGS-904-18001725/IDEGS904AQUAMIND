package com.example.idegs904aquamind.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idegs904aquamind.data.model.LoginResponse
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import com.example.idegs904aquamind.ui.theme.*

/*  Estilo minimalista dominante azul:
    • Fondo PrimaryBlue.
    • Sin card: elementos sueltos con fondo UltraLight y bordes redondeados.
    • Íconos Person y Lock integrados en cada campo.
    • Botón MediumBlue con texto blanco.                                             */

@OptIn(ExperimentalMaterial3Api::class)
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

    /* ---------- Fondo azul completo ---------- */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlue)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* ---------- Encabezado AQUAMIND ---------- */
            Text(
                text = "AQUAMIND",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                color = White
            )
            Spacer(Modifier.height(32.dp))

            /* ---------- Usuario ---------- */
            OutlinedTextField(
                value = username,
                onValueChange = { value ->
                    username = value
                    if (value.isNotBlank()) usernameError = null
                },
                label = { Text("Usuario") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                singleLine = true,
                isError = usernameError != null,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = UltraLight,
                    focusedBorderColor = White,
                    unfocusedBorderColor = LightBlue,
                    cursorColor = PrimaryBlue,
                    focusedLeadingIconColor = PrimaryBlue,
                    unfocusedLeadingIconColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = PrimaryBlue
                )
            )
            usernameError?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }
            Spacer(Modifier.height(20.dp))

            /* ---------- Contraseña ---------- */
            OutlinedTextField(
                value = password,
                onValueChange = { value ->
                    password = value
                    if (value.isNotBlank()) passwordError = null
                },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                isError = passwordError != null,
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = UltraLight,
                    focusedBorderColor = White,
                    unfocusedBorderColor = LightBlue,
                    cursorColor = PrimaryBlue,
                    focusedLeadingIconColor = PrimaryBlue,
                    unfocusedLeadingIconColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    unfocusedLabelColor = PrimaryBlue
                )
            )
            passwordError?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp)
            }
            Spacer(Modifier.height(28.dp))

            /* ---------- Botón ---------- */
            Button(
                onClick = {
                    var valid = true
                    if (username.isBlank()) { usernameError = "Usuario requerido"; valid = false }
                    if (password.isBlank()) { passwordError = "Contraseña requerida"; valid = false }
                    if (valid) viewModel.doLogin(username, password)
                },
                enabled = username.isNotBlank() && password.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MediumBlue,
                    contentColor = White
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "Ingresar")
            }

            /* ---------- Estado de carga / error ---------- */
            Spacer(Modifier.height(12.dp))
            when (uiState) {
                is LoginUiState.Loading -> {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MediumBlue
                    )
                }
                is LoginUiState.Error -> {
                    /* ───── Toast en lugar de texto ───── */
                    LaunchedEffect(uiState) {
                        Toast
                            .makeText(
                                context,
                                "Error de conexión. Intenta de nuevo.",
                                Toast.LENGTH_LONG
                            )
                            .show()
                    }
                }
                else -> {}
            }
        }

        /* ---------- Navegación al éxito ---------- */
        if (uiState is LoginUiState.Success) {
            LaunchedEffect(uiState) {
                onLoginSuccess((uiState as LoginUiState.Success).response)
            }
        }
    }
}