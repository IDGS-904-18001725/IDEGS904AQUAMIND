package com.example.idegs904aquamind.auth.domain

import com.example.idegs904aquamind.auth.data.AuthRepository
import com.example.idegs904aquamind.data.model.LoginResponse

/**
 * Caso de uso para ejecutar el flujo de login de manera manual.
 * Recibe un AuthRepository instanciado con Context.
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) {

    /**
     * Ejecuta el proceso de login con las credenciales proporcionadas.
     *
     * @param username Nombre de usuario o correo.
     * @param password Contraseña asociada al usuario.
     * @return LoginResponse con token y datos de usuario.
     * @throws Exception en caso de error durante la autenticación.
     */
    suspend operator fun invoke(
        username: String,
        password: String
    ): LoginResponse {
        return authRepository.login(username, password)
    }
}
