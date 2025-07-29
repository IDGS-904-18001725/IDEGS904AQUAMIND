package com.example.idegs904aquamind.data.model

/**
 * DTO para enviar las credenciales de login al API.
 *
 * @property username Nombre de usuario o correo utilizado para autenticarse.
 * @property password Contraseña asociada al usuario.
 */
data class LoginRequest(
    val username: String,
    val password: String
)