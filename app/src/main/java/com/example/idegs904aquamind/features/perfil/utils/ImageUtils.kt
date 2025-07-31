package com.example.idegs904aquamind.features.perfil.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utilidades para manejo de imágenes
 */
object ImageUtils {
    
    /**
     * Convierte una imagen desde URI a Base64
     */
    fun convertImageToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            // Redimensionar la imagen para optimizar el tamaño
            val resizedBitmap = resizeBitmap(bitmap, 512, 512)
            
            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            
            val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
            "data:image/jpeg;base64,$base64"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Convierte un Bitmap a Base64
     */
    fun convertBitmapToBase64(bitmap: Bitmap): String {
        // Redimensionar la imagen para optimizar el tamaño
        val resizedBitmap = resizeBitmap(bitmap, 512, 512)
        
        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        
        val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return "data:image/jpeg;base64,$base64"
    }
    
    /**
     * Redimensiona un Bitmap para optimizar el tamaño
     */
    fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        // Si la imagen ya es más pequeña que el máximo, no redimensionar
        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }
        
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
        
        var finalWidth = maxWidth
        var finalHeight = maxHeight
        
        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }
        
        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
    }
    
    /**
     * Verifica si una cadena es un Base64 válido
     */
    fun isValidBase64(base64: String): Boolean {
        return try {
            if (base64.startsWith("data:image/")) {
                val dataPart = base64.substring(base64.indexOf(",") + 1)
                Base64.decode(dataPart, Base64.DEFAULT)
                true
            } else {
                Base64.decode(base64, Base64.DEFAULT)
                true
            }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Formatea una fecha para mostrar solo la fecha sin hora
     */
    fun formatDateOnly(dateString: String?): String {
        if (dateString.isNullOrBlank()) return "No disponible"
        
        return try {
            // Intentar diferentes formatos de fecha
            val formats = listOf(
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd",
                "dd/MM/yyyy",
                "MM/dd/yyyy"
            )
            
            for (format in formats) {
                try {
                    val parser = SimpleDateFormat(format, Locale.getDefault())
                    val date = parser.parse(dateString)
                    if (date != null) {
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        return formatter.format(date)
                    }
                } catch (e: Exception) {
                    // Continuar con el siguiente formato
                }
            }
            
            // Si no se puede parsear, devolver la fecha original
            dateString
        } catch (e: Exception) {
            dateString
        }
    }
} 