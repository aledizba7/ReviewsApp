package com.example.reviewsapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun TodasPeliculasScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }, // Agrega el Bottom Navigation
        containerColor = Color(0xFF121212)
    ) { paddingValues ->
        // Puedes acceder a paddingValues para tu contenido
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Contenido de la pantalla de todas las películas
            androidx.compose.material3.Text(
                text = "Todas las Películas y Reseñas",
                color = Color.White
            )
        }
        }
    }

}
