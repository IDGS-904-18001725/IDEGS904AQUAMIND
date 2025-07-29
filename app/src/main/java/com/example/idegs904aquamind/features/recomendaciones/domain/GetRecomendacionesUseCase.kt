package com.example.idegs904aquamind.features.recomendaciones.domain

import com.example.idegs904aquamind.data.model.Recomendacion
import com.example.idegs904aquamind.features.recomendaciones.data.RecomendacionesRepository

class GetRecomendacionesUseCase(private val repository: RecomendacionesRepository) {
    suspend operator fun invoke(): List<Recomendacion> {
        return repository.getRecomendacionesAleatorias()
    }
} 