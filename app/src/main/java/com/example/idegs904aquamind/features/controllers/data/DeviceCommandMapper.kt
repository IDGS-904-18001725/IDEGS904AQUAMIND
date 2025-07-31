package com.example.idegs904aquamind.features.controllers.data

import com.example.idegs904aquamind.data.model.ControlRequest
import com.example.idegs904aquamind.data.model.Nodo

enum class CommandType {
    ON_OFF,      // Para válvulas y relevadores
    ABRIR_CERRAR // Para compuertas
}

data class DeviceMapping(
    val description: String,
    val deviceCode: String,
    val commandType: CommandType
)

class DeviceCommandMapper {
    private val deviceMappings = mapOf(
        "Válvula 1" to DeviceMapping("Válvula 1", "valve1", CommandType.ON_OFF),
        "Válvula 2" to DeviceMapping("Válvula 2", "valve2", CommandType.ON_OFF),
        "compuerta" to DeviceMapping("compuerta", "gate", CommandType.ABRIR_CERRAR),
        "Relevador B1" to DeviceMapping("Relevador B1", "relay1", CommandType.ON_OFF),
        "Relevador B2" to DeviceMapping("Relevador B2", "relay2", CommandType.ON_OFF)
    )
    
    fun createPayload(nodo: Nodo, newStatus: Int): ControlRequest {
        val mapping = deviceMappings[nodo.descripcion] 
            ?: throw IllegalArgumentException("Dispositivo no mapeado: ${nodo.descripcion}")
        
        val command = when (mapping.commandType) {
            CommandType.ON_OFF -> if (newStatus == 1) "ON" else "OFF"
            CommandType.ABRIR_CERRAR -> if (newStatus == 1) "abrir" else "cerrar"
        }
        
        return ControlRequest(
            device = mapping.deviceCode,
            command = command
        )
    }
    
    fun isDeviceMapped(descripcion: String): Boolean {
        return deviceMappings.containsKey(descripcion)
    }
} 