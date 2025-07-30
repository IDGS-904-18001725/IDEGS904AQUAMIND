package com.example.idegs904aquamind.features.perfil.data.model

/**
 * Modelo de datos del usuario basado en la respuesta de la API
 */
data class Usuario(
    val id_usuario: Int,
    val nombre: String,
    val apellido_paterno: String,
    val apellido_materno: String,
    val correo_electronico: String, // BLOQUEADO - No editable
    val username: String, // BLOQUEADO - No editable
    val fecha_nacimiento: String,
    val fecha_registro: String?,
    val id_estatus: Int,
    val id_tipo_usuario: Int, // CONDICIONAL - Solo editable si es admin
    val imagen_perfil: String?
) 