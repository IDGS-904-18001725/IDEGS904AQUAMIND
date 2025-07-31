package com.example.idegs904aquamind.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idegs904aquamind.auth.data.SessionManager
import com.example.idegs904aquamind.navigation.Screen

@Composable
fun DrawerContent(
    onItemClick: (Screen) -> Unit,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val user = remember { sessionManager.getUser() }
    
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color(0xFF01579B))
            .padding(0.dp)
    ) {
        // Header del usuario
        DrawerHeader(user = user)
        
        // Separador
        Divider(
            color = Color(0xFF039BE5),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        // Lista de elementos del drawer
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(drawerItems) { item ->
                DrawerItem(
                    item = item,
                    onClick = {
                        onItemClick(item.screen)
                        onCloseDrawer()
                    }
                )
            }
        }
        
        // Separador antes del botón de cerrar sesión
        Divider(
            color = Color(0xFF039BE5),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        // Botón de cerrar sesión
        Surface(
            onClick = {
                onLogout()
                onCloseDrawer()
            },
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = Color.Red.copy(alpha = 0.8f),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Título
                Text(
                    text = "Cerrar Sesión",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun DrawerHeader(user: com.example.idegs904aquamind.data.model.User?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Avatar o icono de usuario
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF039BE5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person, // Por defecto usuario normal
                contentDescription = "Usuario",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Nombre del usuario
        Text(
            text = user?.let { "${it.nombre} ${it.apellido}" } ?: "Usuario",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        
        // Email del usuario
        Text(
            text = user?.email ?: "usuario@empresa.com",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp
        )
        
        // Rol del usuario (puedes personalizar según tu lógica)
        Text(
            text = "Usuario",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF039BE5),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DrawerItem(
    item: DrawerItem,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Título
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

data class DrawerItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
)

// Lista de elementos del drawer
private val drawerItems = listOf(
    DrawerItem(
        screen = Screen.Graficos,
        title = "Gráficos",
        icon = Icons.Default.BarChart
    ),
    DrawerItem(
        screen = Screen.Eventos,
        title = "Eventos",
        icon = Icons.Default.Timeline
    ),
    DrawerItem(
        screen = Screen.Recomendaciones,
        title = "Recomendaciones",
        icon = Icons.Default.Lightbulb
    ),
    DrawerItem(
        screen = Screen.History,
        title = "Historial",
        icon = Icons.Default.History
    ),
    DrawerItem(
        screen = Screen.Configuraciones,
        title = "Configuraciones",
        icon = Icons.Default.Settings
    ),
    DrawerItem(
        screen = Screen.Reportes,
        title = "Reportes",
        icon = Icons.Default.Assessment
    ),
    DrawerItem(
        screen = Screen.Mantenimiento,
        title = "Mantenimiento",
        icon = Icons.Default.Build
    ),
    DrawerItem(
        screen = Screen.Soporte,
        title = "Soporte",
        icon = Icons.Default.Support
    ),
    DrawerItem(
        screen = Screen.Ayuda,
        title = "Ayuda",
        icon = Icons.Default.Help
    ),
    DrawerItem(
        screen = Screen.Perfil,
        title = "Perfil",
        icon = Icons.Default.Person
    )
) 