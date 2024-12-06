package com.example.reviewsapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.reviewsapp.R
import com.example.reviewsapp.dtos.MoviesItem
import com.example.reviewsapp.dtos.ReviewItem
import com.example.reviewsapp.dtos.Reviews
import com.example.reviewsapp.services.MovieService
import com.example.reviewsapp.services.ReviewService
import com.example.reviewsapp.use_cases.SharedPref
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodasPeliculasScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    sharedPref: SharedPref
) {
    val scope = rememberCoroutineScope()
    var reviews by remember {
        mutableStateOf(listOf<ReviewItem>())
    }
    var movies by remember {
        mutableStateOf(listOf<MoviesItem>())
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) { //Cambiar esto a usar con RetrofitInstance
        scope.launch {
            try {
                isLoading = true
                val BASE_URL = "http://10.0.2.2:8000/"
                val reviewService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ReviewService::class.java)
                val movieservice = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MovieService::class.java)
                movies = movieservice.getMovies()
                reviews = reviewService.getReviews()
                Log.i("ReviewsResponse", reviews.toString())
                Log.i("MoviesResponse", movies.toString())
                isLoading = false
            } catch (e: Exception) {
                reviews = listOf()
                movies = listOf()
                Log.i("API_ERROR", e.toString())
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Todas las reseñas",
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
                LazyColumn(
                    contentPadding = paddingValues,
                    modifier = Modifier.fillMaxSize()
                ) {

                    items(reviews) {
                        ReviewCard(
                            id = it.id,
                            title = movies[it.movie_id - 1].title, // Tenemos mal las ID creo.
                            image = movies[it.movie_id - 1].image_url,
                            content = it.computedContent,
                            rating = it.rating,
                            onClick = {navController.navigate("reviewDetail/$it")}
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun ReviewCard(
    id: Int,
    title: String,
    image: String,
    content: String,
    rating: Double,
    onClick: (Int) -> Unit
) {
    Card(
        onClick = { onClick(id) },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage( // Por alguna razón no funciona las imagenes importadas aunque escribas la url sin la api, no sé porque sea.
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(image)
                    .placeholder(R.drawable.poster_sample)
                    .build(),
                error = painterResource(R.drawable.poster_sample), //Si se ve este poster es que fallo.
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentDescription = title
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Calificación: $rating/10",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE50914)
                )
            }
        }
    }
}
