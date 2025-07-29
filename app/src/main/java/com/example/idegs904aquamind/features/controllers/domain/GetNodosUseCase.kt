package com.example.idegs904aquamind.features.controllers.domain

import com.example.idegs904aquamind.data.model.Nodo
import com.example.idegs904aquamind.features.controllers.data.ControllersRepository

class GetNodosUseCase(private val repository: ControllersRepository) {
    suspend operator fun invoke(): List<Nodo> {
        return repository.getNodos()
    }
} 