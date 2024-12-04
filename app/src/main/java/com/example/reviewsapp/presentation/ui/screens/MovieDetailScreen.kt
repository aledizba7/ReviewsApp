package com.example.reviewsapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.placeholder
import com.example.reviewsapp.R
import com.example.reviewsapp.dtos.MoviesItem
import com.example.reviewsapp.services.MovieService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(id: Int, innerPaddingValues: PaddingValues,navController: NavController) {
    val scope = rememberCoroutineScope()
    var isLoading by remember {
        mutableStateOf(false)
    }
    var movie by remember {
        mutableStateOf(
            MoviesItem(id = 0, title = "", year = 0, genre = "", rating = 0.0, image_url = "", description = "")
        )
    }
    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                val BASE_URL = "http://10.0.2.2:8000/"
                val movieService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MovieService::class.java)
                isLoading = true
                movie = movieService.getMovieByID(id)
                Log.i("MovieDetailScreen",movie.toString())
                isLoading = false
            }catch (e:Exception) {
                MoviesItem(id = 0, title = "", year = 0, genre = "", rating = 0.0, image_url = "", description = "")
                Log.i("API_ERROR",e.toString())
                isLoading = false
            }

        }
    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .padding(innerPaddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF1B1B1B)
                    )
                )
            },bottomBar = { BottomNavigationBar(navController) },
            containerColor = Color(0xFF121212)
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage( // Por alguna razón no funciona las imagenes importadas aunque escribas la url sin la api, no sé porque sea.
                                model = ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(movie.image_url)
                                    .placeholder(R.drawable.poster_sample)
                                    .build(),
                                error = painterResource(R.drawable.poster_sample), //Si se ve este poster es que fallo.
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentDescription = movie.title)

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )

                            Text(
                                text = "(${movie.year})",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Género: ${movie.genre}",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Sinopsis:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,color = Color.White
                            )
                            Text(
                                text = movie.description,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Rating: ${movie.rating} / 10",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(24.dp))

//                            Button(
//                                onClick = onRateClick,
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Color(0xFFE50914)
//                                )
//                            ) {
//                                Text(
//                                    text = "Calificar",
//                                    color = Color.White
//                                )
//                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun PreviewMovieDetailScreen() {
    MovieDetailScreen(id = 1, innerPaddingValues = PaddingValues(0.dp), navController = rememberNavController())
}