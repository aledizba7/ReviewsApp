package com.example.reviewsapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.reviewsapp.R
import com.example.reviewsapp.dtos.CreateMovie
import com.example.reviewsapp.dtos.CreateReview
import com.example.reviewsapp.dtos.MoviesItem
import com.example.reviewsapp.services.MovieService
import com.example.reviewsapp.services.ReviewService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPeliculasScreen(
    navController: NavController,
    onCreateMovie: (MoviesItem) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var rating by remember { mutableFloatStateOf(0f) }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.background(Color.Black),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Crear Película",
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
                    }) {
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
        },
        bottomBar = { BottomNavigationBar(navController) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Campo para el título
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título de la película") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray, // Gris oscuro para los campos
                        focusedIndicatorColor = Color.Red,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo para el año
                TextField(
                    value = year,
                    onValueChange = { year = it },
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Red,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo para el género
                TextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Género") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Red,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo para la calificación
                Slider(
                    value = rating,
                    onValueChange = { rating = it },
                    steps = 15,
                    valueRange = 0f..10f
                )
                Text(text = rating.toString(), color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))

                // Campo para la descripción de la película
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción de la película") },
                    modifier = Modifier.fillMaxWidth().height(120.dp), // Ajusta el alto según sea necesario
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Red,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedTextColor = Color.White
                    ),
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo para la URL de la imagen
                TextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL de la imagen") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.LightGray,
                        focusedIndicatorColor = Color.Red,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Botón para crear la película
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            try {
                                val movieService = Retrofit.Builder()
                                    .baseUrl("http://10.0.2.2:8000/")
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                    .create(MovieService::class.java)
                                val movie = CreateMovie( title = title, year = year.toInt(), genre = genre, rating = rating, image_url = imageUrl, description = description)
                                val response = movieService.createMovie(movie)
                                Log.i("RegisterScreenAPI",response.toString())
                                navController.navigate("home")
                            }catch (e:Exception) { // Por ahora solo da ERROR.
                                val movie = MoviesItem(id = 0, title = "", year = 0, genre = "", rating = 0.0, image_url = "", description = "")
                                Log.i("API_ERROR",e.toString())
                            }

                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                ) {
                    Text(text = "Crear Película", color = Color.White)
                }
            }
        }
    )
}
