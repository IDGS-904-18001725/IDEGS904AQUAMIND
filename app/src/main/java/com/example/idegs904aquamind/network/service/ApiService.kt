package com.example.idegs904aquamind.network.service

import com.example.idegs904aquamind.data.model.LoginRequest
import com.example.idegs904aquamind.data.model.LoginResponse
import com.example.idegs904aquamind.data.model.Nodo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Definición de los endpoints de la API REST.
 */
interface ApiService {

    /**
     * Endpoint para autenticación: envía credenciales y recibe token + datos de usuario.
     *
     * @param request Objeto con username y password.
     * @return LoginResponse con token JWT y datos del usuario.
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    /**
     * Obtiene la lista de nodos (requiere token de autorización)
     */
    @GET("nodos/nodo")
    suspend fun getNodos(): List<Nodo>
}
