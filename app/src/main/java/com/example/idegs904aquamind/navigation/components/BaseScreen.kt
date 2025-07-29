package com.example.idegs904aquamind.navigation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.idegs904aquamind.navigation.Screen

/**
 * Un Scaffold común con AppHeader y BottomNavBar, que recibe:
 * - screen: para extraer título y ruta
 * - navController: para la BottomNavBar
 * - content: el UI específico de la pantalla
 */
@Composable
fun BaseScreen(
    screen: Screen,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar    = { AppHeader(title = screen.title) },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        content(padding)
    }
}