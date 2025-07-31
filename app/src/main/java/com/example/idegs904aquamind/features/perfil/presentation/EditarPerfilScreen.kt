package com.example.idegs904aquamind.features.perfil.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.features.perfil.data.model.Usuario
import com.example.idegs904aquamind.features.perfil.data.model.ActualizarUsuarioRequest
import com.example.idegs904aquamind.features.perfil.data.model.TipoUsuario
import com.example.idegs904aquamind.features.perfil.presentation.components.ImagePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    usuario: Usuario,
    onGuardar: (ActualizarUsuarioRequest) -> Unit,
    onCancelar: () -> Unit,
    onImageSelected: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var nombre by remember { mutableStateOf(usuario.nombre) }
    var apellidoPaterno by remember { mutableStateOf(usuario.apellido_paterno) }
    var apellidoMaterno by remember { mutableStateOf(usuario.apellido_materno) }
    var fechaNacimiento by remember { mutableStateOf(usuario.fecha_nacimiento) }
    var idTipoUsuario by remember { mutableStateOf(usuario.id_tipo_usuario) }
    var imagenSeleccionada by remember { mutableStateOf<String?>(null) }
    
    // Validaciones
    var nombreError by remember { mutableStateOf("") }
    var apellidoPaternoError by remember { mutableStateOf("") }
    var apellidoMaternoError by remember { mutableStateOf("") }
    
    // Función para manejar la selección de imagen
    val handleImageSelected = { base64: String ->
        imagenSeleccionada = base64
        onImageSelected(base64)
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0277BD))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onCancelar) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Editar Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Formulario
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp), // Espacio para BottomNavBar
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selector de imagen
            ImagePicker(
                currentImageBase64 = imagenSeleccionada ?: usuario.imagen_perfil,
                onImageSelected = handleImageSelected,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            // Campos editables
            CampoEditable(
                label = "Nombre",
                value = nombre,
                onValueChange = { 
                    nombre = it
                    nombreError = if (it.isBlank()) "El nombre es requerido" else ""
                },
                error = nombreError,
                modifier = Modifier.fillMaxWidth()
            )
            
            CampoEditable(
                label = "Apellido Paterno",
                value = apellidoPaterno,
                onValueChange = { 
                    apellidoPaterno = it
                    apellidoPaternoError = if (it.isBlank()) "El apellido paterno es requerido" else ""
                },
                error = apellidoPaternoError,
                modifier = Modifier.fillMaxWidth()
            )
            
            CampoEditable(
                label = "Apellido Materno",
                value = apellidoMaterno,
                onValueChange = { 
                    apellidoMaterno = it
                    apellidoMaternoError = if (it.isBlank()) "El apellido materno es requerido" else ""
                },
                error = apellidoMaternoError,
                modifier = Modifier.fillMaxWidth()
            )
            
            CampoEditable(
                label = "Fecha de Nacimiento",
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo condicional para tipo de usuario
            if (usuario.id_tipo_usuario == TipoUsuario.ADMINISTRADOR.id) { // Si es administrador
                // Dropdown para seleccionar tipo de usuario
                var expanded by remember { mutableStateOf(false) }
                val tiposUsuario = TipoUsuario.values()
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = TipoUsuario.getNombreById(idTipoUsuario),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de Usuario") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        tiposUsuario.forEach { tipo ->
                            DropdownMenuItem(
                                text = { Text(tipo.nombre) },
                                onClick = {
                                    idTipoUsuario = tipo.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            } else {
                // Mostrar como solo lectura si no es admin
                CampoBloqueado(
                    label = "Tipo de Usuario",
                    value = TipoUsuario.getNombreById(usuario.id_tipo_usuario),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Campos bloqueados
            CampoBloqueado(
                label = "Correo Electrónico",
                value = usuario.correo_electronico,
                modifier = Modifier.fillMaxWidth()
            )
            
            CampoBloqueado(
                label = "Nombre de Usuario",
                value = usuario.username,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onCancelar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                
                Button(
                    onClick = {
                        // Validar antes de guardar
                        if (nombre.isNotBlank() && apellidoPaterno.isNotBlank() && apellidoMaterno.isNotBlank()) {
                            val request = ActualizarUsuarioRequest(
                                nombre = nombre,
                                apellido_paterno = apellidoPaterno,
                                apellido_materno = apellidoMaterno,
                                fecha_nacimiento = fechaNacimiento,
                                id_tipo_usuario = if (usuario.id_tipo_usuario == 1) idTipoUsuario else null,
                                imagen_perfil = imagenSeleccionada
                            )
                            onGuardar(request)
                        } else {
                            // Mostrar errores
                            nombreError = if (nombre.isBlank()) "El nombre es requerido" else ""
                            apellidoPaternoError = if (apellidoPaterno.isBlank()) "El apellido paterno es requerido" else ""
                            apellidoMaternoError = if (apellidoMaterno.isBlank()) "El apellido materno es requerido" else ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0277BD)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

@Composable
fun CampoEditable(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            keyboardOptions = keyboardOptions,
            isError = error.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Composable
fun CampoBloqueado(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            label = { Text(label) },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Gray,
                disabledBorderColor = Color.Gray,
                disabledLabelColor = Color.Gray
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Campo no editable",
            color = Color.Gray,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
} 