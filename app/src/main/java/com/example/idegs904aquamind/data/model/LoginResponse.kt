package com.example.idegs904aquamind.data.model

/**
 * DTO de respuesta tras una petición de login exitosa.
 *
 * @property token Token JWT para autenticar peticiones subsecuentes.
 * @property user Información básica del usuario autenticado.
 *                    Se definirá la clase User en próximos pasos según los campos reales.
 */
data class LoginResponse(
    val token: String
    //val user: User
)