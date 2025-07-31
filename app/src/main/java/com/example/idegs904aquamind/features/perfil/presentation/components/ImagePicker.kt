package com.example.idegs904aquamind.features.perfil.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.idegs904aquamind.features.perfil.data.model.TipoUsuario

@Composable
fun ImagePicker(
    isAdmin: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar del usuario
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFF0277BD), CircleShape)
                .background(Color(0xFF0277BD)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                contentDescription = if (isAdmin) "Administrador" else "Usuario",
                modifier = Modifier.size(64.dp),
                tint = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (isAdmin) "Administrador" else "Usuario",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
} 