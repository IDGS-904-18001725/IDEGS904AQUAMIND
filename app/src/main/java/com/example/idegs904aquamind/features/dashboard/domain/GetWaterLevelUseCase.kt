package com.example.idegs904aquamind.features.dashboard.domain

import com.example.idegs904aquamind.data.model.WaterLevelResponse
import com.example.idegs904aquamind.features.dashboard.data.WaterLevelRepository

class GetWaterLevelUseCase(private val repository: WaterLevelRepository) {
    suspend operator fun invoke(): WaterLevelResponse {
        return repository.getWaterLevel()
    }
}
