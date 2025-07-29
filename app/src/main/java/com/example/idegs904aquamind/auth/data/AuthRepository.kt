package com.example.idegs904aquamind.auth.data

import com.example.idegs904aquamind.data.model.LoginRequest
import com.example.idegs904aquamind.data.model.LoginResponse
import com.example.idegs904aquamind.network.service.ApiService
import javax.inject.Inject
import android.content.Context
import com.example.idegs904aquamind.network.service.RetrofitBuilder

/**
 * Repositorio de autenticación que gestiona el flujo de login:
 * - Crea manualmente ApiService y SessionManager usando el Context.
 */
class AuthRepository(private val context: Context) {

    // Instancia de ApiService configurada con interceptores
    private val apiService = RetrofitBuilder.create(context)

    // Gestor de sesión para persistir token
    private val sessionManager = SessionManager(context)

    /**
     * Ejecuta el login con las credenciales proporcionadas.
     * @param username Nombre de usuario o correo.
     * @param password Contraseña asociada.
     * @return LoginResponse con token y datos de usuario.
     */
    suspend fun login(username: String, password: String): LoginResponse {
        // Construye la petición de login
        val request = LoginRequest(username = username, password = password)

        // Llama al endpoint y obtiene la respuesta
        val response: LoginResponse = apiService.login(request)

        // Persiste el token para futuras peticiones
        sessionManager.saveAuthToken(response.token)

        return response
    }
}
