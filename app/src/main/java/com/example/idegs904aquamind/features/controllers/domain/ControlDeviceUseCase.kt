package com.example.idegs904aquamind.features.controllers.domain

import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.features.controllers.data.ControllersRepository

class ControlDeviceUseCase(private val repository: ControllersRepository) {
    suspend operator fun invoke(nodo: Nodo, newStatus: Int): Result<Unit> {
        return repository.controlDevice(nodo, newStatus)
    }
} 