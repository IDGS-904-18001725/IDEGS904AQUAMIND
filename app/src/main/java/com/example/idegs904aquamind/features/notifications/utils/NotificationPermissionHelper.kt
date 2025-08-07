package com.example.idegs904aquamind.features.notifications.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Helper para solicitar permisos de notificación en Android 13+
 */
class NotificationPermissionHelper(private val context: Context) {

    /**
     * Verifica si las notificaciones están habilitadas
     */
    fun estanNotificacionesHabilitadas(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // En versiones anteriores, las notificaciones están habilitadas por defecto
        }
    }

    /**
     * Solicita permisos de notificación si es necesario
     */
    fun solicitarPermisosNotificacion(activity: FragmentActivity, onResult: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!estanNotificacionesHabilitadas()) {
                val launcher = activity.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    onResult(isGranted)
                }
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                onResult(true)
            }
        } else {
            onResult(true)
        }
    }
}
