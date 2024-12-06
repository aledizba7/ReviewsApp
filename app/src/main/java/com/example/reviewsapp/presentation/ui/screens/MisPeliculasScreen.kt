package com.example.reviewsapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.reviewsapp.R
import com.example.reviewsapp.dtos.MoviesItem
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPeliculasScreen(
    movies: List<MoviesItem>,
    navController: NavController,
    onDeleteMovie: (MoviesItem) -> Unit
) {
    var sortBy by remember { mutableStateOf("Año") }

    // Ordenar las películas según la opción seleccionada
    val filteredMovies = movies.sortedWith(compareBy(
        when (sortBy) {
            "Año" -> { it: MoviesItem -> it.year }
            "Género" -> { it: MoviesItem -> it.genre }
            "Calificación" -> { it: MoviesItem -> it.rating }
            else -> { it: MoviesItem -> it.year } // default ordering
        }
    ))

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {Text( //Revisar si en otros celulares se alcanza a ver el texto completo. (en el pequeño no)
                        text = "Crear Reseña",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )},
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
                        containerColor = Color(0xFF1B1B1B)
                    )
                )
                // Ordenar por opciones
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    DropdownMenu(
                        expanded = false,
                        onDismissRequest = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Año", "Género", "Calificación").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = { sortBy = option }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = { BottomNavigationBar(navController) },
        content = {
            LazyColumn(
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(filteredMovies) { movie ->
                    MovieCard(movie = movie, onDeleteMovie = onDeleteMovie, navController = navController)
                }
            }
        }
    )
}

@Composable
fun MovieCard(
    movie: MoviesItem,
    onDeleteMovie: (MoviesItem) -> Unit,
    navController: NavController
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.image_url)
                    .placeholder(R.drawable.poster_sample)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Text(
                text = "(${movie.year})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Género: ${movie.genre}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Rating: ${movie.rating}/10",
                fontSize = 14.sp,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { navController.navigate("movieDetail/${movie.id}") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                ) {
                    Text(text = "Detalles", color = Color.White)
                }
                IconButton(onClick = { onDeleteMovie(movie) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar película",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
