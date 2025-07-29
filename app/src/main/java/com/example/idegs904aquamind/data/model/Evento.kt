package com.example.idegs904aquamind.data.model

data class Evento(
    val fecha: String?, // Para días
    val anio: Int?, // Para años
    val mes: Int?, // Para meses
    val mes_nombre: String?, // Para meses
    val consumo_total: Double? = null, // Hacer opcional
    val unidad_medida: String? = null // Hacer opcional
)

data class FechaRequest(
    val fecha_inicio: String,
    val fecha_fin: String
)

data class PeriodoRequest(
    val cantidad: Int,
    val tipo: Int
)

enum class TipoConsulta {
    INTERVALO,
    PERIODO
}

enum class TipoPeriodo(val id: Int, val label: String) {
    DIAS(1, "Días"),
    MESES(2, "Meses"),
    ANIOS(3, "Años")
} 