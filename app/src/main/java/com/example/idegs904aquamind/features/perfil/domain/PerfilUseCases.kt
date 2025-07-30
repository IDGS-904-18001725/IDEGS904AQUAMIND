package com.example.idegs904aquamind.features.perfil.domain

import com.example.idegs904aquamind.features.perfil.data.PerfilRepository
import com.example.idegs904aquamind.features.perfil.data.model.Usuario
import com.example.idegs904aquamind.features.perfil.data.model.ActualizarUsuarioRequest

/**
 * Caso de uso para obtener información del usuario
 */
class ObtenerUsuarioUseCase(
    private val repository: PerfilRepository
) {
    suspend operator fun invoke(idUsuario: Int): Usuario {
        return repository.obtenerUsuario(idUsuario)
    }
}

/**
 * Caso de uso para actualizar información del usuario
 */
class ActualizarUsuarioUseCase(
    private val repository: PerfilRepository
) {
    suspend operator fun invoke(idUsuario: Int, request: ActualizarUsuarioRequest): Usuario {
        return repository.actualizarUsuario(idUsuario, request)
    }
}

/**
 * Caso de uso para actualizar solo la imagen de perfil
 */
class ActualizarImagenPerfilUseCase(
    private val repository: PerfilRepository
) {
    suspend operator fun invoke(idUsuario: Int, imagenBase64: String): Usuario {
        return repository.actualizarImagenPerfil(idUsuario, imagenBase64)
    }
} 