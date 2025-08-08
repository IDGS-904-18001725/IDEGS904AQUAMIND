package com.example.idegs904aquamind.features.dashboard.data

import android.content.Context
import com.example.idegs904aquamind.data.model.WaterLevelResponse
import com.example.idegs904aquamind.network.service.ApiService
import com.example.idegs904aquamind.network.service.RetrofitBuilder

class WaterLevelRepository(context: Context) {
    private val apiService: ApiService = RetrofitBuilder.getApiService(context)

    suspend fun getWaterLevel(): WaterLevelResponse {
        return apiService.getWaterLevel()
    }
}
