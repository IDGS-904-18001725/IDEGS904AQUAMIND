package com.example.idegs904aquamind.features.dashboard.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DashboardScreen(modifier: Modifier = Modifier) {
    // De momento solo un texto o mock
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Aquí irá tu Dashboard")
    }
}