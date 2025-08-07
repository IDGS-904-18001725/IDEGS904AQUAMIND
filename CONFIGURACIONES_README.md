# Módulo de Configuraciones

## Descripción
El módulo de configuraciones permite administrar los parámetros del sistema de niveles de agua. Permite configurar 4 niveles diferentes con validaciones automáticas.

## Configuraciones Disponibles

### 1. Nivel Máximo Actualizado (ID: 1)
- **Descripción**: Altura máxima del contenedor
- **Valor por defecto**: 20
- **Unidad de medida**: CM (Centímetros)
- **Validaciones**: 
  - Solo debe ser un número decimal válido
  - No puede ser menor a 0

### 2. Nivel Alto (ID: 2)
- **Descripción**: Umbral de nivel alto
- **Valor por defecto**: 80
- **Unidad de medida**: % (Porcentaje)
- **Validaciones**:
  - No puede ser menor a 0
  - No puede ser mayor a 100%

### 3. Nivel Normal (ID: 3)
- **Descripción**: Umbral de nivel normal
- **Valor por defecto**: 60
- **Unidad de medida**: % (Porcentaje)
- **Validaciones**:
  - No puede ser menor a 0
  - No puede ser mayor que el Nivel Alto

### 4. Nivel Bajo (ID: 4)
- **Descripción**: Umbral de nivel bajo
- **Valor por defecto**: 40
- **Unidad de medida**: % (Porcentaje)
- **Validaciones**:
  - No puede ser menor a 0
  - No puede ser mayor que el Nivel Normal

## Endpoints Utilizados

### GET /api/v1/configuraciones/configuracion
Obtiene todas las configuraciones del sistema.

**Respuesta**:
```json
[
    {
        "configuracion": "AlturaContenedor",
        "descripcion": "Nivel máximo actualizado",
        "id_configuracion": 1,
        "id_estatus": 1,
        "valor": "20"
    },
    {
        "configuracion": "umbral_alto_nivel",
        "descripcion": "Nivel Alto",
        "id_configuracion": 2,
        "id_estatus": 1,
        "valor": "80"
    },
    {
        "configuracion": "umbral_normal_nivel",
        "descripcion": "Nivel Normal",
        "id_configuracion": 3,
        "id_estatus": 1,
        "valor": "60"
    },
    {
        "configuracion": "umbral_bajo_nivel",
        "descripcion": "Nivel Bajo",
        "id_configuracion": 4,
        "id_estatus": 1,
        "valor": "40"
    }
]
```

### PUT /api/v1/configuraciones/configuracion/{id}
Actualiza una configuración específica.

**Body**:
```json
{
    "valor": "20"
}
```

**Respuesta**:
```json
{
    "configuracion": "AlturaContenedor",
    "descripcion": "Nivel máximo actualizado",
    "id_configuracion": 1,
    "id_estatus": 1,
    "valor": "20"
}
```

## Características del Módulo

### Validaciones Automáticas
- Los valores se validan en tiempo real
- Se muestran mensajes de error específicos
- Los inputs se deshabilitan durante la actualización
- **Botón de guardar**: Aparece solo cuando hay cambios válidos
- **Validaciones específicas**:
  - Input 1: Solo validación numérica (CM)
  - Input 2: Máximo 100% (Porcentaje)
  - Input 3: Máximo el valor del Nivel Alto (Porcentaje)
  - Input 4: Máximo el valor del Nivel Normal (Porcentaje)

### Interfaz de Usuario
- Diseño moderno y amigable
- Indicadores de carga y error
- **Botón de guardar** que aparece solo cuando hay cambios
- Validaciones visuales con colores
- **Unidades de medida** mostradas en cada input:
  - CM para el primer input (Nivel Máximo)
  - % para los demás inputs (Niveles Alto, Normal, Bajo)

### Arquitectura
- **ViewModel**: Maneja el estado y la lógica de negocio
- **Repository**: Gestiona las operaciones de red
- **Use Cases**: Encapsulan la lógica de negocio
- **Components**: Componentes reutilizables para los inputs

## Flujo de Funcionamiento

1. **Carga Inicial**: Al abrir la pantalla, se cargan automáticamente las configuraciones desde el servidor
2. **Validación**: Cada input valida su valor en tiempo real
3. **Cambios**: Al modificar un valor, aparece un botón de "Guardar"
4. **Guardado**: Al hacer clic en "Guardar", se envía el valor al servidor
5. **Feedback**: Se muestran indicadores de carga y errores según corresponda

## Estructura de Archivos

```
features/configuraciones/
├── data/
│   └── ConfiguracionesRepository.kt
├── domain/
│   └── ConfiguracionesUseCases.kt
└── presentation/
    ├── components/
    │   └── ConfiguracionInput.kt
    ├── ConfiguracionesScreen.kt
    ├── ConfiguracionesViewModel.kt
    └── ConfiguracionesViewModelFactory.kt
```

## Dependencias

- **Retrofit**: Para las llamadas HTTP
- **Moshi**: Para el parsing JSON
- **Coroutines**: Para operaciones asíncronas
- **Compose**: Para la interfaz de usuario
- **ViewModel**: Para el manejo de estado
