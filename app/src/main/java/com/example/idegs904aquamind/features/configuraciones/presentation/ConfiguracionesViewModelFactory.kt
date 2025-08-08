package com.example.idegs904aquamind.features.configuraciones.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.idegs904aquamind.features.configuraciones.data.ConfiguracionesRepository
import com.example.idegs904aquamind.features.configuraciones.data.ConfiguracionesLocalManager
import com.example.idegs904aquamind.features.configuraciones.domain.ActualizarConfiguracionUseCase
import com.example.idegs904aquamind.features.configuraciones.domain.GetConfiguracionesUseCase
import com.example.idegs904aquamind.features.notifications.service.SimpleNotificationScheduler
import com.example.idegs904aquamind.network.service.RetrofitBuilder

/**
 * Factory para crear ConfiguracionesViewModel con las dependencias necesarias
 */
class ConfiguracionesViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfiguracionesViewModel::class.java)) {
            val apiService = RetrofitBuilder.getApiService(context)
            val repository = ConfiguracionesRepository(apiService)
            val getConfiguracionesUseCase = GetConfiguracionesUseCase(repository)
            val actualizarConfiguracionUseCase = ActualizarConfiguracionUseCase(repository)
            val configuracionesLocalManager = ConfiguracionesLocalManager(context)
            val notificationScheduler = SimpleNotificationScheduler(context)
            
            @Suppress("UNCHECKED_CAST")
            return ConfiguracionesViewModel(
                getConfiguracionesUseCase = getConfiguracionesUseCase,
                actualizarConfiguracionUseCase = actualizarConfiguracionUseCase,
                configuracionesLocalManager = configuracionesLocalManager,
                notificationScheduler = notificationScheduler
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
