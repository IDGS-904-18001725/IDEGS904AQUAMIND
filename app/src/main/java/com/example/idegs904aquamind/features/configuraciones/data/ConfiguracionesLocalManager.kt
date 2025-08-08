package com.example.idegs904aquamind.features.configuraciones.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

// Delegado para crear el DataStore de Preferences para configuraciones
private val Context.configuracionesDataStore by preferencesDataStore(name = "configuraciones_prefs")

/**
 * Gestor de configuraciones locales usando DataStore.
 * Maneja configuraciones que se almacenan localmente en el dispositivo.
 */
class ConfiguracionesLocalManager(private val context: Context) {

    companion object {
        private val ACTUALIZACIONES_AUTOMATICAS_KEY = booleanPreferencesKey("actualizaciones_automaticas")
        private val FRECUENCIA_NOTIFICACIONES_KEY = intPreferencesKey("frecuencia_notificaciones")
    }

    /**
     * Obtiene el estado de las actualizaciones automáticas de forma síncrona.
     * @return true si están activadas, false si están desactivadas
     */
    fun getActualizacionesAutomaticas(): Boolean = runBlocking {
        val prefs = context.configuracionesDataStore.data.first()
        prefs[ACTUALIZACIONES_AUTOMATICAS_KEY] ?: false // Por defecto desactivado
    }

    /**
     * Obtiene el estado de las actualizaciones automáticas como Flow.
     * @return Flow<Boolean> con el estado actual
     */
    fun getActualizacionesAutomaticasFlow(): Flow<Boolean> {
        return context.configuracionesDataStore.data.map { prefs ->
            prefs[ACTUALIZACIONES_AUTOMATICAS_KEY] ?: false // Por defecto desactivado
        }
    }

    /**
     * Establece el estado de las actualizaciones automáticas.
     * @param enabled true para activar, false para desactivar
     */
    suspend fun setActualizacionesAutomaticas(enabled: Boolean) {
        context.configuracionesDataStore.edit { prefs ->
            prefs[ACTUALIZACIONES_AUTOMATICAS_KEY] = enabled
        }
    }

    /**
     * Establece el estado de las actualizaciones automáticas de forma síncrona.
     * @param enabled true para activar, false para desactivar
     */
    fun setActualizacionesAutomaticasSync(enabled: Boolean) {
        runBlocking {
            setActualizacionesAutomaticas(enabled)
        }
    }

    /**
     * Obtiene la frecuencia de notificaciones en segundos.
     * @return Frecuencia en segundos (por defecto 300)
     */
    fun getFrecuenciaNotificaciones(): Int = runBlocking {
        val prefs = context.configuracionesDataStore.data.first()
        prefs[FRECUENCIA_NOTIFICACIONES_KEY] ?: 300 // Por defecto 300 segundos (5 minutos)
    }

    /**
     * Obtiene la frecuencia de notificaciones como Flow.
     * @return Flow<Int> con la frecuencia actual
     */
    fun getFrecuenciaNotificacionesFlow(): Flow<Int> {
        return context.configuracionesDataStore.data.map { prefs ->
            prefs[FRECUENCIA_NOTIFICACIONES_KEY] ?: 300 // Por defecto 300 segundos (5 minutos)
        }
    }

    /**
     * Establece la frecuencia de notificaciones.
     * @param segundos Frecuencia en segundos
     */
    suspend fun setFrecuenciaNotificaciones(segundos: Int) {
        context.configuracionesDataStore.edit { prefs ->
            prefs[FRECUENCIA_NOTIFICACIONES_KEY] = segundos
        }
    }

    /**
     * Establece la frecuencia de notificaciones de forma síncrona.
     * @param segundos Frecuencia en segundos
     */
    fun setFrecuenciaNotificacionesSync(segundos: Int) {
        runBlocking {
            setFrecuenciaNotificaciones(segundos)
        }
    }

    /**
     * Resetea todas las configuraciones a los valores por defecto.
     */
    suspend fun resetearConfiguraciones() {
        context.configuracionesDataStore.edit { prefs ->
            prefs.remove(ACTUALIZACIONES_AUTOMATICAS_KEY)
            prefs.remove(FRECUENCIA_NOTIFICACIONES_KEY)
        }
    }
}
