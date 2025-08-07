package com.example.idegs904aquamind.auth.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.idegs904aquamind.data.model.User
import com.example.idegs904aquamind.features.notifications.service.NotificationScheduler
import com.google.gson.Gson
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
        // Clave para almacenar datos del usuario en JSON
        private val USER_KEY = stringPreferencesKey("user_data")
        // Clave para almacenar el tipo de token
        private val TOKEN_TYPE_KEY = stringPreferencesKey("token_type")
    }

    private val gson = Gson()

    /**
     * Persiste el token JWT y datos del usuario en DataStore.
     *
     * @param token Token recibido tras el login.
     * @param type Tipo de token (Bearer).
     * @param user Datos del usuario.
     */
    suspend fun saveAuthData(token: String, type: String, user: User) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[TOKEN_TYPE_KEY] = type
            prefs[USER_KEY] = gson.toJson(user)
        }
    }

    /**
     * Persiste solo el token JWT en DataStore (método legacy).
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
     * Recupera los datos del usuario de forma síncrona.
     *
     * @return Datos del usuario o null si no existe.
     */
    fun getUser(): User? = runBlocking {
        val prefs = context.dataStore.data.first()
        val userJson = prefs[USER_KEY]
        userJson?.let { gson.fromJson(it, User::class.java) }
    }

    /**
     * Recupera el nombre completo del usuario.
     *
     * @return Nombre completo o null si no existe.
     */
    fun getUserFullName(): String? {
        val user = getUser()
        return user?.let { "${it.nombre} ${it.apellido}" }
    }

    /**
     * Limpia la sesión, removiendo token y datos de usuario.
     */
    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(TOKEN_TYPE_KEY)
            prefs.remove(USER_KEY)
        }
        
        // Detener verificaciones de notificaciones al cerrar sesión
        val notificationScheduler = NotificationScheduler(context)
        notificationScheduler.detenerVerificacionesPeriodicas()
    }
}
