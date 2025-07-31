package com.example.idegs904aquamind.features.controllers.data

import android.content.Context
import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.network.service.RetrofitBuilder

class ControllersRepository(private val context: Context) {
    private val apiService = RetrofitBuilder.create(context)
    private val deviceCommandMapper = DeviceCommandMapper()

    suspend fun getNodos(): List<Nodo> {
        return apiService.getNodos()
    }
    
    suspend fun controlDevice(nodo: Nodo, newStatus: Int): Result<Unit> {
        return try {
            val payload = deviceCommandMapper.createPayload(nodo, newStatus)
            val response = apiService.controlDevice(payload)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error del servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 