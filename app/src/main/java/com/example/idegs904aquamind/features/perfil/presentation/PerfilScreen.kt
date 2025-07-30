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

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    viewModel: PerfilViewModel = viewModel(
        factory = PerfilViewModelFactory(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val usuario by viewModel.usuario.collectAsState()
    var mostrarEditarPerfil by remember { mutableStateOf(false) }
    
    // Observar cambios en el estado
    LaunchedEffect(uiState) {
        when (uiState) {
            is PerfilUiState.CerrandoSesion -> {
                // TODO: Navegar a LoginScreen
            }
            is PerfilUiState.Error -> {
                // TODO: Mostrar snackbar con error
            }
            else -> {}
        }
    }
    
    if (mostrarEditarPerfil && usuario != null) {
        EditarPerfilScreen(
            usuario = usuario!!,
            onGuardar = { request ->
                viewModel.actualizarUsuario(request)
                mostrarEditarPerfil = false
            },
            onCancelar = {
                mostrarEditarPerfil = false
            },
            onImageSelected = { base64 ->
                viewModel.actualizarImagenPerfil(base64)
            }
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
                        CircularProgressIndicator()
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
                                    "Fecha de Nacimiento: ${user.fecha_nacimiento}",
                                    "Tipo de Usuario: ${if (user.id_tipo_usuario == 1) "Administrador" else "Usuario"}"
                                )
                            )
                            
                            // Información de cuenta
                            PerfilCard(
                                titulo = "Información de Cuenta",
                                items = listOf(
                                    "Usuario: ${user.username}",
                                    "ID: ${user.id_usuario}",
                                    "Estado: ${if (user.id_estatus == 1) "Activo" else "Inactivo"}",
                                    "Fecha de Registro: ${user.fecha_registro ?: "No disponible"}"
                                )
                            )
                            
                            // Imagen de perfil (si existe)
                            if (user.imagen_perfil != null) {
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
                                color = Color.Red
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