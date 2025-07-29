package com.example.idegs904aquamind.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import com.example.idegs904aquamind.auth.data.SessionManager
import javax.inject.Inject

/**
 * Interceptor de autenticación para OkHttp.
 * Inyecta el token de sesión en el header "Authorization" de cada petición.
 */
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Clona la petición original
        val requestBuilder = chain.request().newBuilder()

        // Obtén el token (DataStore o cache interno)
        // IMPORTANTE: SessionManager.getToken() debe devolver el token de forma síncrona
        val token: String? = sessionManager.getToken() // TODO: implementar método sync o manejar cache

        // Si hay token, agrégalo al header
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        // Continúa con la petición modificada
        return chain.proceed(requestBuilder.build())
    }
}