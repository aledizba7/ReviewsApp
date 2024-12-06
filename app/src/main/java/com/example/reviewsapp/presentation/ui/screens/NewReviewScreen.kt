package com.example.reviewsapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.reviewsapp.R
import com.example.reviewsapp.dtos.CreateReview
import com.example.reviewsapp.dtos.MoviesItem
import com.example.reviewsapp.services.MovieService
import com.example.reviewsapp.services.ReviewService
import com.example.reviewsapp.use_cases.SharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReviewScreen(
    id : Int,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref
){
    var movie by remember {
        mutableStateOf(
            MoviesItem(id = 0, title = "", year = 0, genre = "", rating = 0.0, image_url = "", description = "")
        )
    }
    var content by remember {
        mutableStateOf("")
    }
    var rating by remember {
        mutableFloatStateOf(0f)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }

    val isButtonEnabled = content.isNotEmpty()
    val scope = rememberCoroutineScope()

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF2A2A2A)),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
//                    AsyncImage( // Por alguna razón no funciona las imagenes importadas aunque escribas la url sin la api, no sé porque sea.
//                        model = ImageRequest
//                            .Builder(LocalContext.current)
//                            .data(movie.image_url)
//                            .placeholder(R.drawable.poster_sample)
//                            .build(),
//                        error = painterResource(R.drawable.poster_sample), //Si se ve este poster es que fallo.
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(350.dp)
//                            .clip(RoundedCornerShape(8.dp)),
//                        contentDescription = movie.title)
                    Spacer(modifier = Modifier.height(35.dp))
                    TextField(
                        value = content,
                        onValueChange = { content= it }, modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(10.dp)
                            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Slider(
                        value = rating,
                        onValueChange = { rating = it },
                        steps = 15,
                        valueRange = 0f..10f
                    )
                    Text(text = rating.toString(), color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        enabled = isButtonEnabled,
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val reviewService = Retrofit.Builder()
                                        .baseUrl("http://10.0.2.2:8000/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()
                                        .create(ReviewService::class.java)
                                    val review = CreateReview(content = content, rating = rating.toDouble(), movie_id = id)
                                    val response = reviewService.createReview(title = movie.title,review)
                                    Log.i("RegisterScreenAPI",response.toString())
                                    navController.navigate("home")
                                }catch (e:Exception) { // Por ahora solo da ERROR.
                                    val review = CreateReview(content = "", rating = 5.0, movie_id = id)
                                    Log.i("API_ERROR",e.toString())
                                    isLoading = false
                                }

                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(text = "Calificar", fontSize = 17.sp)
                    }
                }
            }
        }

    }
}