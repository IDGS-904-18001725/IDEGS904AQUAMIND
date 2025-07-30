package com.example.idegs904aquamind.features.notifications.domain

import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.features.notifications.data.NotificacionesRepository

class MarcarNotificacionLeidaUseCase(
    private val repository: NotificacionesRepository
) {
    suspend operator fun invoke(id: Int): Notificacion {
        return repository.marcarNotificacionLeida(id)
    }
} 