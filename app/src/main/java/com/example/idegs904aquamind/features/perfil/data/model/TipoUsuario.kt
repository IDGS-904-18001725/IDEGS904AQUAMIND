package com.example.idegs904aquamind.features.perfil.data.model

/**
 * Catálogo de tipos de usuario
 */
enum class TipoUsuario(val id: Int, val nombre: String) {
    USUARIO_NORMAL(1, "Usuario Normal"),
    ADMINISTRADOR(2, "Administrador");
    
    companion object {
        /**
         * Obtiene el tipo de usuario por ID
         */
        fun fromId(id: Int): TipoUsuario? {
            return values().find { it.id == id }
        }
        
        /**
         * Obtiene el nombre del tipo de usuario por ID
         */
        fun getNombreById(id: Int): String {
            return fromId(id)?.nombre ?: "Desconocido"
        }
    }
} 