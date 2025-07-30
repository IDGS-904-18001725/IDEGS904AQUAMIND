package com.example.idegs904aquamind.features.notifications.data

import android.content.Context
import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.data.model.MarcarLeidaRequest
import com.example.idegs904aquamind.network.service.RetrofitBuilder

class NotificacionesRepository(private val context: Context) {
    private val apiService = RetrofitBuilder.create(context)

    suspend fun getNotificaciones(): List<Notificacion> {
        return apiService.getNotificaciones()
    }

    suspend fun getNotificacionesPorEstatus(estatus: Int): List<Notificacion> {
        return apiService.getNotificacionesPorEstatus(estatus)
    }

    suspend fun marcarNotificacionLeida(id: Int): Notificacion {
        val request = MarcarLeidaRequest(id_estatus = 2)
        return apiService.marcarNotificacionLeida(id, request)
    }
} 