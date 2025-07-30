package com.example.idegs904aquamind.features.perfil.data.model

/**
 * Modelo para actualizar datos del usuario
 * Solo incluye campos editables (excluye username y correo_electronico)
 */
data class ActualizarUsuarioRequest(
    val nombre: String? = null,
    val apellido_paterno: String? = null,
    val apellido_materno: String? = null,
    val fecha_nacimiento: String? = null,
    val id_tipo_usuario: Int? = null, // Solo si es administrador
    val password: String? = null,
    val imagen_perfil: String? = null // Base64 de la imagen
) 