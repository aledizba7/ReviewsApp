package com.example.reviewsapp.presentation.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage

@Composable
fun MisPeliculasScreen(navController: NavController) {
    var savedMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }

    LaunchedEffect(Unit) {
        savedMovies = getSavedMovies()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Contenido de la pantalla de las películas del usuario
        Text(
            text = "Mis Películas y Reseñas",
            color = Color.White
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columnas para la cartelera
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Ajusta el padding según sea necesario
    ) {
        items(savedMovies) { movie ->
            // Card para cada película con diseño de cartelera
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Ajusta la altura según sea necesario
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.DarkGray // Fondo oscuro para la cartelera
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagen de la película
                    AsyncImage(
                        model = movie.posterUrl, // Reemplazar con la URL de la imagen
                        contentDescription = movie.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp) // Ajusta la altura según sea necesario
                    )

                    // Título de la película
                    Text(
                        text = movie.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMisPeliculasScreen() {
    MisPeliculasScreen(rememberNavController())
}

data class Movie(
    val title: String,
    val posterUrl: String
)

suspend fun getSavedMovies(): List<Movie> {
    // Reemplaza con tu lógica para obtener las películas de la API
    return listOf(
        Movie(title = "Película 1", posterUrl = "https://example.com/poster1.jpg"),
        Movie(title = "Película 2", posterUrl = "https://example.com/poster2.jpg")
    )
}

