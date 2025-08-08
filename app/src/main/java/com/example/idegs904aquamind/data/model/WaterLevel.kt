package com.example.idegs904aquamind.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WaterLevelResponse(
    val success: Boolean,
    val data: WaterLevelData,
    val historial_7_dias: List<WaterLevelHistorial>,
    val interpretacion: WaterLevelInterpretacion
)

@JsonClass(generateAdapter = true)
data class WaterLevelData(
    val bomba: String,
    val compuerta: String,
    val desnivel: String,
    val distancia: Double,
    val fecha: String,
    val id_nivel: Int,
    val nivel_estado: String,
    val porcentaje_agua: Double
)

@JsonClass(generateAdapter = true)
data class WaterLevelHistorial(
    val bomba: String,
    val compuerta: String,
    val desnivel: String,
    val distancia: Double,
    val fecha: String,
    val id_nivel: Int,
    val nivel_estado: String,
    val porcentaje_agua: Double
)

@JsonClass(generateAdapter = true)
data class WaterLevelInterpretacion(
    val color: String,
    val descripcion: String,
    val dispositivos: Dispositivos,
    val medicion: Medicion,
    val nivel: String,
    val recomendacion: String
)

@JsonClass(generateAdapter = true)
data class Dispositivos(
    val bomba: Dispositivo,
    val compuerta: Dispositivo
)

@JsonClass(generateAdapter = true)
data class Dispositivo(
    val estado: String,
    val descripcion: String
)

@JsonClass(generateAdapter = true)
data class Medicion(
    val distancia: String,
    val interpretacion: String
)
