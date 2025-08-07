package com.example.idegs904aquamind.features.notifications.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.idegs904aquamind.MainActivity
import com.example.idegs904aquamind.R
import com.example.idegs904aquamind.data.model.Notificacion
import com.example.idegs904aquamind.navigation.Screen

/**
 * Helper para crear y mostrar notificaciones en la barra de estado.
 * Configurado para demostración con alta prioridad y visibilidad.
 */
class NotificationHelper(private val context: Context) {

    companion object {
        private const val TAG = "NotificationHelper"
        private const val CHANNEL_ID = "aquamind_notifications"
        private const val CHANNEL_NAME = "AquaMind Notificaciones"
        private const val CHANNEL_DESCRIPTION = "Notificaciones del sistema AquaMind"
        private const val NOTIFICATION_ID_BASE = 1000
    }

    init {
        crearCanalNotificacion()
    }

    /**
     * Crea el canal de notificación para Android 8.0+
     */
    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            
            Log.d(TAG, "Canal de notificación creado: $CHANNEL_ID")
        }
    }

    /**
     * Muestra una notificación en la barra de estado
     */
    fun mostrarNotificacion(notificacion: Notificacion) {
        try {
            val notificationId = NOTIFICATION_ID_BASE + notificacion.id_notificacion
            
            // Crear intent para abrir la app en la pantalla de notificaciones
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("navigate_to", Screen.Notifications.route)
                putExtra("notification_id", notificacion.id_notificacion)
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Construir la notificación
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(notificacion.notificacion)
                .setContentText(notificacion.mensaje)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificacion.mensaje))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build()

            // Mostrar la notificación
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                NotificationManagerCompat.from(context).notify(notificationId, notification)
                Log.d(TAG, "Notificación mostrada: ${notificacion.notificacion}")
            } else {
                Log.w(TAG, "Las notificaciones están deshabilitadas")
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error mostrando notificación: ${e.message}", e)
        }
    }

    /**
     * Muestra una notificación de prueba para verificar el sistema
     */
    fun mostrarNotificacionPrueba() {
        val notificacionPrueba = Notificacion(
            fecha_notificacion = "Wed, 30 Jul 2025 11:42:14 GMT",
            id_estatus = 1,
            id_notificacion = 999,
            mensaje = "Esta es una notificación de prueba del sistema AquaMind",
            notificacion = "Prueba de Notificación"
        )
        
        mostrarNotificacion(notificacionPrueba)
        Log.d(TAG, "Notificación de prueba enviada")
    }

    /**
     * Cancela una notificación específica
     */
    fun cancelarNotificacion(notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
        Log.d(TAG, "Notificación cancelada: $notificationId")
    }

    /**
     * Cancela todas las notificaciones de la app
     */
    fun cancelarTodasLasNotificaciones() {
        NotificationManagerCompat.from(context).cancelAll()
        Log.d(TAG, "Todas las notificaciones canceladas")
    }
}
