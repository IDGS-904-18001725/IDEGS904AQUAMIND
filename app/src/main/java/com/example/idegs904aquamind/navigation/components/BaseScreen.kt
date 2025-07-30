package com.example.idegs904aquamind.navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.idegs904aquamind.navigation.Screen
import kotlinx.coroutines.launch

/**
 * Un Scaffold común con AppHeader, Drawer y BottomNavBar, que recibe:
 * - screen: para extraer título y ruta
 * - navController: para la navegación
 * - content: el UI específico de la pantalla
 */
@Composable
fun BaseScreen(
    screen: Screen,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    var isDrawerOpen by remember { mutableStateOf(false) }
    
    // Solo mostrar drawer en pantallas principales (no en login)
    val showDrawer = screen != Screen.Login
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Contenido principal
        Scaffold(
            topBar = {
                AppHeader(
                    title = screen.title,
                    onMenuClick = {
                        if (showDrawer) {
                            isDrawerOpen = !isDrawerOpen
                        }
                    },
                    onProfileClick = {
                        navController.navigate(Screen.Perfil.route)
                    }
                )
            },
            bottomBar = { BottomNavBar(navController) }
        ) { padding ->
            content(padding)
        }
        
        // Overlay semi-transparente cuando el drawer está abierto
        AnimatedVisibility(
            visible = isDrawerOpen && showDrawer,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { isDrawerOpen = false }
                    .zIndex(1f)
            ) {
                // Área del drawer
                AnimatedVisibility(
                    visible = isDrawerOpen,
                    enter = slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300)
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .width(280.dp)
                            .fillMaxHeight()
                            .background(
                                color = Color(0xFF01579B),
                                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                            )
                            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                            .zIndex(2f)
                    ) {
                        DrawerContent(
                            onItemClick = { destination ->
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                isDrawerOpen = false
                            },
                            onCloseDrawer = {
                                isDrawerOpen = false
                            }
                        )
                    }
                }
            }
        }
    }
}