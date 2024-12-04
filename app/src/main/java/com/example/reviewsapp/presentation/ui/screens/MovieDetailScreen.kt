package com.example.reviewsapp.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.reviewsapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    idMovie: Int,
    image: Int,
    title: String,
    year: String,
    synopsis: String,
    genre: String,
    rating: Float,
    navController: NavController,
    onRateClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
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
                        Image(painter = painterResource(id = image),
                            contentDescription = "Movie Poster",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )

                        Text(
                            text = "($year)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Género: $genre",
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
                            text = synopsis,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Rating: $rating / 5",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onRateClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE50914)
                            )
                        ) {
                            Text(
                                text = "Calificar",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMovieDetailScreen() {
    MovieDetailScreen(
        idMovie = 1,
        image = R.drawable.poster_sample,
        title = "Inception",
        year = "2010",
        synopsis = "Un ladrón con la habilidad de entrar en los sueños de las personas toma el trabajo de su vida: implantar una idea en la mente de un CEO.",
        genre = "Ciencia Ficción",
        rating = 4.8f,
        navController = rememberNavController(),
    ) {
    }
}