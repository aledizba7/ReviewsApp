package com.example.reviewsapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodasPeliculasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Todas las películas",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        // Cerrar sesión
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true // Elimina las pantallas anteriores del stack
                            }
                        }
                    } ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1B1B1B) // Barra superior oscura
                )
            )
        },
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
