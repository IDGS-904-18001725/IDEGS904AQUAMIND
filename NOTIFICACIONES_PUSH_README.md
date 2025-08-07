# Sistema de Notificaciones Push - AquaMind

## Descripción

Sistema de notificaciones push que verifica cada 30 segundos si hay nuevas notificaciones no leídas en el endpoint:
```
GET https://genuine-liberation-production.up.railway.app/api/v1/notificaciones/estatus/1
```

## Características

- ✅ **Verificación cada 30 segundos** (ideal para demostración)
- ✅ **Notificaciones en barra de estado** con sonido y vibración
- ✅ **Control automático** - se inicia al hacer login, se detiene al cerrar sesión
- ✅ **Interfaz de control** en pantalla de notificaciones
- ✅ **Sin duplicados** - control por timestamp
- ✅ **Logs detallados** para debugging

## Componentes Implementados

### 1. NotificationWorker
- Verifica periódicamente el endpoint
- Filtra notificaciones nuevas por timestamp
- Muestra notificaciones en barra de estado

### 2. NotificationHelper
- Crea y muestra notificaciones
- Configura canal de alta prioridad
- Maneja intents para abrir la app

### 3. NotificationScheduler
- Programa verificaciones cada 30 segundos
- Controla inicio/detención del sistema
- Proporciona estado actual

### 4. NotificationStatusCard
- Muestra estado del sistema en tiempo real
- Botones para controlar verificaciones
- Indicador visual de estado activo/inactivo

## Cómo Usar

### 1. Inicio Automático
- El sistema se inicia automáticamente al hacer login
- Se detiene automáticamente al cerrar sesión

### 2. Control Manual
- Ve a la pantalla **Notificaciones**
- Usa la **NotificationStatusCard** para:
  - Ver estado actual del sistema
  - Iniciar/detener verificaciones
  - Ejecutar verificación inmediata

### 3. Prueba del Sistema
- En pantalla de notificaciones, toca **"Probar Notificación"**
- Debería aparecer una notificación de prueba en la barra de estado

### 4. Logs de Debug
- Abre **Logcat** en Android Studio
- Filtra por tags:
  - `NotificationWorker`
  - `NotificationHelper`
  - `NotificationScheduler`

## Configuración

### Permisos Requeridos
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

### Dependencias
```kotlin
implementation("androidx.work:work-runtime-ktx:2.9.0")
```

## Flujo de Funcionamiento

```
1. Usuario hace login → Sistema inicia verificaciones
2. Cada 30s → GET /notificaciones/estatus/1
3. Si hay nuevas → Compara con timestamp guardado
4. Si son nuevas → Crea notificación en barra de estado
5. Usuario ve notificación → Tap abre app en pantalla notificaciones
6. Usuario cierra sesión → Sistema detiene verificaciones
```

## Personalización

### Cambiar Frecuencia
En `NotificationScheduler.kt`:
```kotlin
private const val FREQUENCY_SECONDS = 30L // Cambiar aquí
```

### Modificar Notificaciones
En `NotificationHelper.kt`:
```kotlin
// Cambiar icono, sonido, vibración, etc.
```

### Agregar Acciones
En `NotificationHelper.kt`:
```kotlin
// Agregar botones de acción a las notificaciones
```

## Troubleshooting

### Las notificaciones no aparecen
1. Verifica permisos en configuración del dispositivo
2. Revisa logs en Logcat
3. Usa "Probar Notificación" para verificar el sistema

### Verificaciones no funcionan
1. Verifica conexión a internet
2. Revisa logs del NotificationWorker
3. Usa "Verificar Ahora" para prueba inmediata

### Error de API
1. Verifica que el endpoint esté funcionando
2. Revisa logs de red en Logcat
3. Verifica token de autenticación

## Logs Importantes

```
NotificationWorker: Iniciando verificación de notificaciones...
NotificationWorker: Encontradas X notificaciones no leídas
NotificationWorker: Encontradas X notificaciones nuevas
NotificationHelper: Notificación mostrada: [título]
NotificationScheduler: Verificaciones periódicas programadas exitosamente
```

## Notas de Demostración

- **Frecuencia alta**: 30 segundos para respuesta inmediata
- **Sin optimizaciones**: Verifica constantemente sin restricciones de batería
- **Logs detallados**: Para debugging y monitoreo
- **Control visual**: Estado visible en la interfaz

¡El sistema está listo para demostración! 🚀
