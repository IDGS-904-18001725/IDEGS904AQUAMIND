package com.example.idegs904aquamind.network.service

import com.example.idegs904aquamind.data.model.LoginRequest
import com.example.idegs904aquamind.data.model.LoginResponse
import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.data.model.Evento
import com.example.idegs904aquamind.data.model.FechaRequest
import com.example.idegs904aquamind.data.model.Recomendacion
import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.data.model.MarcarLeidaRequest
import com.example.idegs904aquamind.data.model.ControlRequest
import com.example.idegs904aquamind.data.model.Configuracion
import com.example.idegs904aquamind.data.model.ActualizarConfiguracionRequest
import com.example.idegs904aquamind.data.model.WaterLevelResponse
import com.example.idegs904aquamind.features.perfil.data.model.Usuario
import com.example.idegs904aquamind.features.perfil.data.model.ActualizarUsuarioRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    // Endpoints de notificaciones
    /**
     * Obtiene todas las notificaciones
     */
    @GET("notificaciones/notificacion")
    suspend fun getNotificaciones(): List<Notificacion>

    /**
     * Obtiene notificaciones por estatus
     */
    @GET("notificaciones/estatus/{estatus}")
    suspend fun getNotificacionesPorEstatus(@Path("estatus") estatus: Int): List<Notificacion>

    /**
     * Marca una notificación como leída
     */
    @PUT("notificaciones/notificacion/{id}")
    suspend fun marcarNotificacionLeida(
        @Path("id") id: Int,
        @Body request: MarcarLeidaRequest
    ): Notificacion

    // Endpoints de perfil de usuario
    /**
     * Obtiene la información del usuario por ID
     */
    @GET("usuarios/usuario/{id}")
    suspend fun obtenerUsuario(@Path("id") id: Int): Usuario

    /**
     * Actualiza la información del usuario
     */
    @PUT("usuarios/usuario/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Body request: ActualizarUsuarioRequest
    ): Usuario

    /**
     * Controla un dispositivo MQTT
     */
    @POST("mqtt/control")
    suspend fun controlDevice(@Body request: ControlRequest): retrofit2.Response<Unit>

    // Endpoints de configuraciones
    /**
     * Obtiene todas las configuraciones del sistema
     */
    @GET("configuraciones/configuracion")
    suspend fun getConfiguraciones(): List<Configuracion>

    /**
     * Actualiza una configuración específica
     */
    @PUT("configuraciones/configuracion/{id}")
    suspend fun actualizarConfiguracion(
        @Path("id") id: Int,
        @Body request: ActualizarConfiguracionRequest
    ): Configuracion

    @GET("niveles-agua/actual-con-historial")
    suspend fun getWaterLevel(): WaterLevelResponse
}
