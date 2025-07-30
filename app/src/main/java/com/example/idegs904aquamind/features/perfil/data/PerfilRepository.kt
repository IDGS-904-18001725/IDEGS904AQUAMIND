package com.example.idegs904aquamind.features.perfil.data

import com.example.idegs904aquamind.features.perfil.data.model.Usuario
import com.example.idegs904aquamind.features.perfil.data.model.ActualizarUsuarioRequest
import com.example.idegs904aquamind.network.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository para manejar operaciones del perfil de usuario
 */
class PerfilRepository(
    private val apiService: ApiService
) {
    
    /**
     * Obtiene la información del usuario por ID
     */
    suspend fun obtenerUsuario(idUsuario: Int): Usuario = withContext(Dispatchers.IO) {
        apiService.obtenerUsuario(idUsuario)
    }
    
    /**
     * Actualiza la información del usuario
     */
    suspend fun actualizarUsuario(idUsuario: Int, request: ActualizarUsuarioRequest): Usuario = withContext(Dispatchers.IO) {
        apiService.actualizarUsuario(idUsuario, request)
    }
    
    /**
     * Actualiza solo la imagen de perfil
     */
    suspend fun actualizarImagenPerfil(idUsuario: Int, imagenBase64: String): Usuario = withContext(Dispatchers.IO) {
        val request = ActualizarUsuarioRequest(imagen_perfil = imagenBase64)
        apiService.actualizarUsuario(idUsuario, request)
    }
} 