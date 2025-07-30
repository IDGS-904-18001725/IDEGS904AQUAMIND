package com.example.idegs904aquamind.data.model

/**
 * Modelo de datos para una notificación.
 */
data class Notificacion(
    val fecha_notificacion: String, // Formato: "Wed, 30 Jul 2025 11:42:14 GMT"
    val id_estatus: Int,           // 1 = no leída, 2 = leída
    val id_notificacion: Int,      // ID único de la notificación
    val mensaje: String,           // Contenido del mensaje
    val notificacion: String       // Título de la notificación
)

/**
 * Request para marcar una notificación como leída.
 */
data class MarcarLeidaRequest(
    val id_estatus: Int
) 