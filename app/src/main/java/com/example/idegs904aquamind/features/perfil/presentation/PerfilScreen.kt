package com.example.idegs904aquamind.features.perfil.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.idegs904aquamind.features.perfil.data.model.TipoUsuario
import com.example.idegs904aquamind.features.perfil.utils.ImageUtils

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    viewModel: PerfilViewModel = viewModel(
        factory = PerfilViewModelFactory(LocalContext.current)
    ),
    onNavigateToLogin: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val usuario by viewModel.usuario.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()
    var mostrarEditarPerfil by remember { mutableStateOf(false) }
    var mostrarSnackbar by remember { mutableStateOf<String?>(null) }
    
    // Observar cambios en el estado
    LaunchedEffect(uiState) {
        when (uiState) {
            is PerfilUiState.CerrandoSesion -> {
                // La navegación se maneja a través de navigationEvent
            }
            is PerfilUiState.Error -> {
                mostrarSnackbar = (uiState as PerfilUiState.Error).message
            }
            is PerfilUiState.Success -> {
                // Limpiar mensajes de error si hay éxito
                if (mostrarSnackbar != null) {
                    mostrarSnackbar = null
                }
            }
            else -> {}
        }
    }
    
    // Observar eventos de navegación
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is PerfilNavigationEvent.NavigateToLogin -> {
                onNavigateToLogin()
                viewModel.clearNavigationEvent()
            }
            else -> {}
        }
    }
    
    if (mostrarEditarPerfil && usuario != null) {
        EditarPerfilScreen(
            usuario = usuario!!,
            onGuardar = { request ->
                viewModel.actualizarUsuario(request)
                // Recargar datos después de guardar para asegurar que se muestre la imagen actualizada
                viewModel.recargarUsuario()
                mostrarEditarPerfil = false
            },
            onCancelar = {
                mostrarEditarPerfil = false
            },

        )
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0277BD))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Perfil de Usuario",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Contenido
            when (uiState) {
                is PerfilUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (uiState is PerfilUiState.CerrandoSesion) "Cerrando sesión..." else "Cargando...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                is PerfilUiState.Success -> {
                    usuario?.let { user ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Información personal
                            PerfilCard(
                                titulo = "Información Personal",
                                items = listOf(
                                    "Nombre: ${user.nombre} ${user.apellido_paterno} ${user.apellido_materno}",
                                    "Email: ${user.correo_electronico}",
                                    "Fecha de Nacimiento: ${ImageUtils.formatDateOnly(user.fecha_nacimiento)}",
                                    "Tipo de Usuario: ${TipoUsuario.getNombreById(user.id_tipo_usuario)}"
                                )
                            )
                            
                            // Información de cuenta
                            PerfilCard(
                                titulo = "Información de Cuenta",
                                items = listOf(
                                    "Usuario: ${user.username}",
                                    "ID: ${user.id_usuario}",
                                    "Estado: ${if (user.id_estatus == 1) "Activo" else "Inactivo"}",
                                    "Fecha de Registro: ${ImageUtils.formatDateOnly(user.fecha_registro)}"
                                )
                            )
                            
                            // Imagen de perfil (si existe)
                            if (user.imagen_perfil != null && user.imagen_perfil.isNotBlank()) {
                                PerfilCard(
                                    titulo = "Imagen de Perfil",
                                    items = listOf(
                                        "Imagen disponible"
                                    )
                                )
                            }
                            
                            // Acciones
                            Button(
                                onClick = { mostrarEditarPerfil = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0277BD)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar Perfil")
                            }
                            
                            Button(
                                onClick = { viewModel.cerrarSesion() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Red
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cerrar Sesión")
                            }
                        }
                    }
                }
                is PerfilUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = (uiState as PerfilUiState.Error).message,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.recargarUsuario() }
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
    
    // Snackbar para mostrar mensajes
    mostrarSnackbar?.let { message ->
        LaunchedEffect(message) {
            // Aquí podrías mostrar un Snackbar si lo implementas
            // Por ahora solo limpiamos el mensaje después de un tiempo
            kotlinx.coroutines.delay(3000)
            mostrarSnackbar = null
        }
    }
}

@Composable
fun PerfilCard(
    titulo: String,
    items: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            items.forEach { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
} 