package com.example.idegs904aquamind.data.model

/**
 * DTO de respuesta tras una petición de login exitosa.
 *
 * @property token Token JWT para autenticar peticiones subsecuentes.
 * @property type Tipo de token (Bearer).
 * @property user Información del usuario autenticado.
 */
data class LoginResponse(
    val token: String,
    val type: String,
    val user: User
)

/**
 * Modelo de datos del usuario.
 */
data class User(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val username: String
)