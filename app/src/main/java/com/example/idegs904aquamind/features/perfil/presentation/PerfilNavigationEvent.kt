package com.example.idegs904aquamind.features.perfil.presentation

/**
 * Eventos de navegación para el módulo de perfil
 */
sealed class PerfilNavigationEvent {
    object NavigateToLogin : PerfilNavigationEvent()
    object NavigateBack : PerfilNavigationEvent()
} 