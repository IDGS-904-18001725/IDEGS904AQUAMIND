package com.example.idegs904aquamind.auth.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// Delegado para crear el DataStore de Preferences con el nombre "session_prefs"
private val Context.dataStore by preferencesDataStore(name = "session_prefs")

/**
 * Administra la sesión de usuario: guarda y recupera token y datos de usuario
 * usando DataStore Preferences.
 */
class SessionManager(private val context: Context) {

    companion object {
        // Clave para almacenar el token de autenticación
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        // Clave para almacenar datos adicionales del usuario en JSON (opcional)
        private val USER_KEY  = stringPreferencesKey("user_data")
    }

    /**
     * Persiste el token JWT en DataStore.
     *
     * @param token Token recibido tras el login.
     */
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    /**
     * Recupera el token de forma síncrona.
     * Nota: usa runBlocking; en un entorno real podrías cachearlo
     * o exponer un Flow para evitar bloquear el hilo.
     *
     * @return Token JWT o null si no existe.
     */
    fun getToken(): String? = runBlocking {
        val prefs = context.dataStore.data.first()
        prefs[TOKEN_KEY]
    }

    /**
     * Limpia la sesión, removiendo token y datos de usuario.
     */
    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(USER_KEY)
        }
    }
}
