package com.example.reviewsapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.reviewsapp.use_cases.SharedPref

@Composable
fun UsuarioScreen(navController: NavController, sharedPref: SharedPref) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true // Elimina pantallas anteriores del stack
                    }
                }
            }
        ) {
            androidx.compose.material3.Text(text = "Cerrar sesión", color = Color.White)
        }
    }
}
