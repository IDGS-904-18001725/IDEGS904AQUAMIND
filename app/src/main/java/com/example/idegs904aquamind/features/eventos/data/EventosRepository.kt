package com.example.idegs904aquamind.features.eventos.data

import android.content.Context
import com.example.idegs904aquamind.data.model.Evento
import com.example.idegs904aquamind.data.model.FechaRequest
import com.example.idegs904aquamind.network.service.RetrofitBuilder

class EventosRepository(private val context: Context) {
    private val apiService = RetrofitBuilder.create(context)

    suspend fun getEventosPorFechas(fechaInicio: String, fechaFin: String): List<Evento> {
        val request = FechaRequest(fecha_inicio = fechaInicio, fecha_fin = fechaFin)
        return apiService.getEventosPorFechas(request)
    }

    suspend fun getEventosPorPeriodo(cantidad: Int, tipo: Int): List<Evento> {
        return apiService.getEventosPorPeriodo(cantidad, tipo)
    }
} 