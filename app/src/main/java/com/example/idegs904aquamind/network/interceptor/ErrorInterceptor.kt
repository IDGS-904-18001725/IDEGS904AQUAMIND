package com.example.idegs904aquamind.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Interceptor para manejo global de errores HTTP.
 * Si la respuesta no es exitosa (código fuera de 200..299),
 * lanza una excepción para ser capturada por el cliente.
 */
class ErrorInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Ejecuta la petición original
        val response: Response = chain.proceed(chain.request())

        // Verifica si el código HTTP indica éxito
        if (!response.isSuccessful) {
            // Lee el cuerpo de error, si existe
            val errorBody = response.body?.string()
            // Construye el mensaje de excepción
            val errorMessage = "HTTP ${response.code}: ${response.message}" +
                    (errorBody?.let { " - $it" } ?: "")
            // Lanza IOException con detalles del error
            throw IOException(errorMessage)
        }

        // Si es exitoso, retorna la respuesta normalmente
        return response
    }
}
