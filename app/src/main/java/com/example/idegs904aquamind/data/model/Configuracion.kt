package com.example.idegs904aquamind.data.model

/**
 * Modelo de datos para las configuraciones del sistema.
 *
 * @property id_configuracion ID único de la configuración
 * @property configuracion Nombre de la configuración
 * @property descripcion Descripción de la configuración
 * @property valor Valor de la configuración (como string)
 * @property id_estatus Estado de la configuración
 */
data class Configuracion(
    val id_configuracion: Int,
    val configuracion: String,
    val descripcion: String,
    val valor: String,
    val id_estatus: Int
)

/**
 * Request para actualizar una configuración.
 *
 * @property valor Nuevo valor de la configuración
 */
data class ActualizarConfiguracionRequest(
    val valor: String
)
