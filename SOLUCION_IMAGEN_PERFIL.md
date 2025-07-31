# Solución para el Problema de Imágenes de Perfil

## Problema Identificado

El formato Base64 que se está guardando es correcto:
```
data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/4gHYSUNDX1BST0ZJTEU.....//Z
```

Sin embargo, la imagen no se muestra correctamente después de guardarse. Esto se debe a varios problemas en el manejo del estado:

## Problemas Identificados

### 1. **Estado Local no se Actualiza**
- Cuando se guarda la imagen, el estado local `imagenSeleccionada` en `EditarPerfilScreen` no se actualiza con la respuesta del servidor
- El `ImagePicker` sigue mostrando la imagen anterior en lugar de la nueva

### 2. **Manejo de Errores en Carga de Imágenes**
- No hay manejo de errores cuando falla la carga de imágenes Base64
- Si la imagen está corrupta o mal formateada, la aplicación puede crashear

### 3. **Recarga de Datos**
- Después de guardar, no se recargan los datos del servidor para asegurar que se muestre la imagen actualizada

## Soluciones Implementadas

### 1. **Mejora en EditarPerfilScreen**

```kotlin
// Función para obtener la imagen actual a mostrar
val imagenActual = imagenSeleccionada ?: usuario.imagen_perfil

// Uso en ImagePicker
ImagePicker(
    currentImageBase64 = imagenActual,
    onImageSelected = handleImageSelected,
    modifier = Modifier.align(Alignment.CenterHorizontally)
)
```

### 2. **Recarga de Datos después de Guardar**

```kotlin
onGuardar = { request ->
    viewModel.actualizarUsuario(request)
    // Recargar datos después de guardar para asegurar que se muestre la imagen actualizada
    viewModel.recargarUsuario()
    mostrarEditarPerfil = false
},
```

### 3. **Manejo de Errores en ImagePicker**

```kotlin
currentImageBase64 != null && currentImageBase64.isNotBlank() -> {
    // Mostrar imagen actual desde Base64
    try {
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
    } catch (e: Exception) {
        // Si hay error al cargar la imagen, mostrar placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Error al cargar imagen",
                modifier = Modifier.size(48.dp),
                tint = Color.Gray
            )
        }
    }
}
```

## Verificaciones Adicionales Recomendadas

### 1. **Verificar el Formato Base64**
```kotlin
// En ImageUtils.kt
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

### 2. **Logs para Debugging**
```kotlin
// Agregar logs para verificar el flujo
Log.d("ImagePicker", "Current image: ${currentImageBase64?.take(50)}...")
Log.d("ImagePicker", "Selected image: ${imagenSeleccionada?.take(50)}...")
```

### 3. **Verificar Respuesta del Servidor**
```kotlin
// En PerfilViewModel
fun actualizarUsuario(request: ActualizarUsuarioRequest) {
    viewModelScope.launch {
        try {
            _uiState.value = PerfilUiState.Loading
            
            val usuarioActualizado = actualizarUsuarioUseCase(idUsuarioActual, request)
            Log.d("PerfilViewModel", "Usuario actualizado: ${usuarioActualizado.imagen_perfil?.take(50)}...")
            _usuario.value = usuarioActualizado
            _uiState.value = PerfilUiState.Success
            
        } catch (e: Exception) {
            _uiState.value = PerfilUiState.Error(e.message ?: "Error al actualizar el perfil")
        }
    }
}
```

## Pasos para Probar

1. **Seleccionar una imagen nueva**
   - Verificar que se muestra correctamente en el ImagePicker
   - Verificar que se guarda al servidor

2. **Guardar el perfil**
   - Verificar que se recargan los datos
   - Verificar que la imagen se muestra en el perfil principal

3. **Recargar la aplicación**
   - Verificar que la imagen persiste después de cerrar y abrir la app

4. **Probar con diferentes formatos**
   - JPEG, PNG, diferentes tamaños
   - Verificar que se optimizan correctamente

## Posibles Causas del Problema

1. **Problema en el Servidor**: El servidor podría estar devolviendo una imagen vacía o corrupta
2. **Problema de Caché**: Coil podría estar cacheando una versión anterior
3. **Problema de Formato**: El Base64 podría estar mal formateado
4. **Problema de Estado**: El estado local no se está actualizando correctamente

## Recomendaciones

1. **Agregar logs detallados** para rastrear el flujo de datos
2. **Verificar la respuesta del servidor** directamente
3. **Implementar un mecanismo de fallback** para imágenes corruptas
4. **Agregar indicadores de carga** mientras se procesan las imágenes
5. **Validar el Base64** antes de intentar mostrarlo

## Código de Verificación

```kotlin
// Función para verificar si una imagen Base64 es válida
fun isImageBase64Valid(base64: String?): Boolean {
    if (base64.isNullOrBlank()) return false
    
    return try {
        if (base64.startsWith("data:image/")) {
            val dataPart = base64.substring(base64.indexOf(",") + 1)
            val decoded = Base64.decode(dataPart, Base64.DEFAULT)
            decoded.size > 0
        } else {
            val decoded = Base64.decode(base64, Base64.DEFAULT)
            decoded.size > 0
        }
    } catch (e: Exception) {
        false
    }
}
``` 