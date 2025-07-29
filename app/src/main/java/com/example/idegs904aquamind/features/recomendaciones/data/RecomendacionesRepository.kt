package com.example.idegs904aquamind.features.recomendaciones.data

import android.content.Context
import com.example.idegs904aquamind.data.model.Recomendacion
import com.example.idegs904aquamind.network.service.RetrofitBuilder

class RecomendacionesRepository(private val context: Context) {
    private val apiService = RetrofitBuilder.create(context)

    suspend fun getRecomendacionesAleatorias(): List<Recomendacion> {
        return apiService.getRecomendacionesAleatorias()
    }
} 