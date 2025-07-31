# Correcciones del Módulo de Perfil y Sesión

## Problemas Identificados y Soluciones

### 1. Problema con las Imágenes de Perfil

**Problema:** Las imágenes no se guardaban correctamente o no se interpretaban bien.

**Causas:**
- El `ImagePicker` no manejaba correctamente las imágenes Base64
- No había optimización de imágenes antes de enviarlas
- El estado de la imagen seleccionada no se actualizaba correctamente

**Soluciones Implementadas:**

#### a) Mejora del ImagePicker (`ImagePicker.kt`)
```kotlin
// Antes
currentImageBase64: String?

// Después  
currentImageBase64: String?,
onImageSelected: (String) -> Unit

// Mejor manejo de imágenes Base64
Image(
    painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(currentImageBase64)
            .build()
    ),
    contentDescription = "Imagen de perfil actual",
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Crop
)
```

#### b) Optimización de Imágenes (`ImageUtils.kt`)
```kotlin
// Redimensionamiento automático de imágenes
fun convertImageToBase64(context: Context, uri: Uri): String? {
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val resizedBitmap = resizeBitmap(bitmap, 512, 512) // Máximo 512x512
    // ... resto del código
}

// Verificación de Base64 válido
fun isValidBase64(base64: String): Boolean {
    return try {
        if (base64.startsWith("data:image/")) {
            val dataPart = base64.substring(base64.indexOf(",") + 1)
            Base64.decode(dataPart, Base64.DEFAULT)
            true
        } else {
            Base64.decode(base64, Base64.DEFAULT)
            true
        }
    } catch (e: Exception) {
        false
    }
}
```

#### c) Mejora del Estado en EditarPerfilScreen
```kotlin
// Función para manejar la selección de imagen
val handleImageSelected = { base64: String ->
    imagenSeleccionada = base64
    onImageSelected(base64)
}

// Uso correcto del estado
ImagePicker(
    currentImageBase64 = imagenSeleccionada ?: usuario.imagen_perfil,
    onImageSelected = handleImageSelected,
    modifier = Modifier.align(Alignment.CenterHorizontally)
)
```

### 2. Problema con Cerrar Sesión

**Problema:** Al cerrar sesión, la aplicación no navegaba correctamente al login y no limpiaba el stack de navegación.

**Causas:**
- No había manejo específico de navegación al login desde el perfil
- El stack de navegación no se limpiaba completamente
- No había opción de cerrar sesión desde el drawer

**Soluciones Implementadas:**

#### a) Mejora del AppNavHost (`AppNavHost.kt`)
```kotlin
// Manejo específico de navegación al login desde perfil
composable(Screen.Perfil.route) {
    BaseScreen(
        screen = Screen.Perfil,
        navController = navController
    ) { padding ->
        PerfilScreen(
            modifier = Modifier.padding(padding),
            onNavigateToLogin = {
                // Limpiar todo el stack de navegación y ir al login
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}
```

#### b) Mejora del PerfilViewModel (`PerfilViewModel.kt`)
```kotlin
fun cerrarSesion() {
    viewModelScope.launch {
        try {
            _uiState.value = PerfilUiState.Loading
            
            // Limpiar sesión
            sessionManager.clearSession()
            
            // Limpiar datos del usuario
            _usuario.value = null
            idUsuarioActual = 0
            
            _uiState.value = PerfilUiState.CerrandoSesion
            
            // Emitir evento de navegación
            _navigationEvent.value = PerfilNavigationEvent.NavigateToLogin
            
        } catch (e: Exception) {
            _uiState.value = PerfilUiState.Error(e.message ?: "Error al cerrar sesión")
        }
    }
}
```

#### c) Agregado Botón de Cerrar Sesión en Drawer (`DrawerContent.kt`)
```kotlin
@Composable
fun DrawerContent(
    onItemClick: (Screen) -> Unit,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit = {}, // Nueva función
    modifier: Modifier = Modifier
) {
    // ... contenido del drawer ...
    
    // Botón de cerrar sesión al final
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
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Cerrar sesión",
                tint = Color.Red.copy(alpha = 0.8f),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = "Cerrar Sesión",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Red.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
```

#### d) Manejo del Logout en BaseScreen (`BaseScreen.kt`)
```kotlin
// Función para manejar el logout
val handleLogout = {
    // Navegar al login y limpiar el stack
    navController.navigate(Screen.Login.route) {
        popUpTo(0) { inclusive = true }
    }
}

// Pasar la función al drawer
DrawerContent(
    onItemClick = { destination ->
        // ... navegación normal
    },
    onCloseDrawer = {
        isDrawerOpen = false
    },
    onLogout = {
        handleLogout()
    }
)
```

### 3. Mejoras Adicionales

#### a) Mejor Manejo de Estados (`PerfilScreen.kt`)
```kotlin
// Estados más descriptivos
when (uiState) {
    is PerfilUiState.Loading -> {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (uiState is PerfilUiState.CerrandoSesion) 
                    "Cerrando sesión..." else "Cargando...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    // ... otros estados
}
```

#### b) Validación de Imágenes
```kotlin
// Verificar que la imagen existe y no está vacía
if (user.imagen_perfil != null && user.imagen_perfil.isNotBlank()) {
    PerfilCard(
        titulo = "Imagen de Perfil",
        items = listOf("Imagen disponible")
    )
}
```

## Resultados Esperados

1. **Imágenes de Perfil:**
   - Las imágenes se cargan y muestran correctamente
   - Se optimizan automáticamente antes de enviar al servidor
   - El estado se actualiza correctamente al seleccionar una nueva imagen

2. **Cerrar Sesión:**
   - Funciona desde el botón en el perfil
   - Funciona desde el drawer (nueva opción)
   - Limpia completamente el stack de navegación
   - No permite regresar con los controles de navegación del dispositivo

3. **Experiencia de Usuario:**
   - Estados de carga más informativos
   - Mejor manejo de errores
   - Feedback visual mejorado

## Archivos Modificados

1. `EditarPerfilScreen.kt` - Mejor manejo de estado de imágenes
2. `ImagePicker.kt` - Mejor renderizado de imágenes Base64
3. `ImageUtils.kt` - Optimización y validación de imágenes
4. `AppNavHost.kt` - Manejo de navegación al login
5. `PerfilViewModel.kt` - Mejor manejo de estados y logout
6. `PerfilScreen.kt` - Estados más informativos
7. `DrawerContent.kt` - Agregado botón de cerrar sesión
8. `BaseScreen.kt` - Manejo del logout desde drawer

## Pruebas Recomendadas

1. **Probar imágenes de perfil:**
   - Seleccionar una imagen desde la galería
   - Verificar que se muestra correctamente
   - Verificar que se guarda al servidor
   - Verificar que se carga al recargar el perfil

2. **Probar cerrar sesión:**
   - Desde el botón en el perfil
   - Desde el drawer
   - Verificar que no se puede regresar con el botón atrás
   - Verificar que se limpia la sesión

3. **Probar estados de carga:**
   - Verificar mensajes informativos durante carga
   - Verificar manejo de errores
   - Verificar feedback visual 