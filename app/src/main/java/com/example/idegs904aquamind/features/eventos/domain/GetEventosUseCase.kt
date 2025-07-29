package com.example.idegs904aquamind.features.eventos.domain

import com.example.idegs904aquamind.data.model.Evento
import com.example.idegs904aquamind.data.model.TipoConsulta
import com.example.idegs904aquamind.features.eventos.data.EventosRepository

class GetEventosUseCase(private val repository: EventosRepository) {
    
    suspend fun getEventosPorFechas(fechaInicio: String, fechaFin: String): List<Evento> {
        return repository.getEventosPorFechas(fechaInicio, fechaFin)
    }
    
    suspend fun getEventosPorPeriodo(cantidad: Int, tipo: Int): List<Evento> {
        return repository.getEventosPorPeriodo(cantidad, tipo)
    }
} 