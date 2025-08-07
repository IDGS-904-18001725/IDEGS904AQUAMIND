package com.example.idegs904aquamind.features.configuraciones.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.data.model.Configuracion

/**
 * Componente para input de configuración con validaciones.
 */
@Composable
fun ConfiguracionInput(
    configuracion: Configuracion,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    maxValue: Double? = null,
    minValue: Double = 0.0,
    isUpdating: Boolean = false,
    unidadMedida: String = ""
) {
    var inputValue by remember { mutableStateOf(configuracion.valor) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var hasChanges by remember { mutableStateOf(false) }

    // Actualizar inputValue cuando cambie la configuración
    LaunchedEffect(configuracion.valor) {
        inputValue = configuracion.valor
        hasChanges = false
    }

    // Validar el valor cuando cambie
    LaunchedEffect(inputValue) {
        val valor = inputValue.toDoubleOrNull()
        when {
            valor == null -> {
                errorMessage = "El valor debe ser un número válido"
                isError = true
            }
            valor < minValue -> {
                errorMessage = "El valor no puede ser menor a $minValue"
                isError = true
            }
            maxValue != null && valor > maxValue -> {
                errorMessage = "El valor no puede ser mayor a $maxValue"
                isError = true
            }
            else -> {
                errorMessage = null
                isError = false
                hasChanges = inputValue != configuracion.valor
            }
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isError) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título y descripción
            Text(
                text = configuracion.descripcion,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Input con unidad de medida
            OutlinedTextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth(),
                isError = isError,
                enabled = !isUpdating,
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                ),
                suffix = {
                    if (unidadMedida.isNotEmpty()) {
                        Text(
                            text = unidadMedida,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
            
            // Mensaje de error
            if (isError && errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = errorMessage!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
                }
            }
            
            // Botón de guardar y indicador de actualización
            if (hasChanges && !isError) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onValueChange(inputValue)
                            onSave()
                        },
                        enabled = !isUpdating,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (isUpdating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardando...")
                        } else {
                            Text("Guardar")
                        }
                    }
                }
            } else if (isUpdating) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Actualizando...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
