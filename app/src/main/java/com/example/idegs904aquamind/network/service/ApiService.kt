package com.example.idegs904aquamind.network.service

import com.example.idegs904aquamind.data.model.LoginRequest
import com.example.idegs904aquamind.data.model.LoginResponse
import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.data.model.Evento
import com.example.idegs904aquamind.data.model.FechaRequest
import com.example.idegs904aquamind.data.model.Recomendacion
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    /**
     * Obtiene eventos por intervalo de fechas
     */
    @POST("eventos/evento/fechas")
    suspend fun getEventosPorFechas(@Body request: FechaRequest): List<Evento>

    /**
     * Obtiene eventos por periodicidad
     */
    @GET("eventos/evento/periodo/{cantidad}/tipo/{tipo}")
    suspend fun getEventosPorPeriodo(
        @Path("cantidad") cantidad: Int,
        @Path("tipo") tipo: Int
    ): List<Evento>

    /**
     * Obtiene recomendaciones aleatorias
     */
    @GET("recomendaciones/aleatorias/10")
    suspend fun getRecomendacionesAleatorias(): List<Recomendacion>
}
