package com.example.idegs904aquamind.features.notifications.domain

import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.features.notifications.data.NotificacionesRepository

class GetNotificacionesUseCase(
    private val repository: NotificacionesRepository
) {
    suspend operator fun invoke(): List<Notificacion> {
        return repository.getNotificaciones()
    }

    suspend operator fun invoke(estatus: Int): List<Notificacion> {
        return repository.getNotificacionesPorEstatus(estatus)
    }
} 