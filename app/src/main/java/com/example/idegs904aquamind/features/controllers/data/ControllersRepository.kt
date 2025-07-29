package com.example.idegs904aquamind.features.controllers.data

import android.content.Context
import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.network.service.RetrofitBuilder

class ControllersRepository(private val context: Context) {
    private val apiService = RetrofitBuilder.create(context)

    suspend fun getNodos(): List<Nodo> {
        return apiService.getNodos()
    }
} 