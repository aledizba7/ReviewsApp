package com.example.reviewsapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.reviewsapp.R
import com.example.reviewsapp.use_cases.SharedPref
import com.example.reviewsapp.presentation.ui.theme.ReviewsAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.placeholder
import com.example.reviewsapp.dtos.MoviesItem
import com.example.reviewsapp.services.LoginService
import com.example.reviewsapp.services.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavController,
    sharedPref: SharedPref
) {
    val scope = rememberCoroutineScope()
    var movies by remember {
        mutableStateOf(listOf<MoviesItem>())
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var history = remember {
        mutableListOf("Spider-Man: No Way Home")
    }
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        scope.launch {
            try {
                isLoading = true
                val BASE_URL = "http://10.0.2.2:8000/"
                val movieService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MovieService::class.java)
                movies = movieService.getMovies()
                Log.i("HomeScreenResponse", movies.toString())
                isLoading = false
            } catch (e:Exception) {
                movies = listOf()
                Log.i("API_ERROR",e.toString())
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    /*Text( //Revisar si en otros celulares se alcanza a ver el texto completo. (en el pequeño no)
                        text = "Explorar Películas",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )*/
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
            SearchBar(modifier = Modifier.width(300.dp).offset(x = 50.dp) //Search bar
                ,query = searchText,
                onQueryChange = {
                    searchText = it
                },
                onSearch = {
                    history.add(searchText)
                    active = false
                },
                active = active,
                placeholder = {Text("Buscar Películas")},
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = { Icon(modifier = Modifier.clickable {
                    if(searchText.isNotEmpty()){
                        searchText = ""
                    } else {
                        active = false
                    }
                }, imageVector = Icons.Default.Clear, contentDescription = "Clear") },
                onActiveChange = {
                    active = it
                }) {
                history.forEach { // Historial del search bar
                    Row (modifier = Modifier.padding(all = 14.dp).clickable { searchText = it }){
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History"
                        )
                        Text(text = it)
                    }
                }


            }
        },

        bottomBar = { BottomNavigationBar(navController) }, // Aquí agregamos el BottomNavigationBar
        containerColor = Color(0xFF121212)
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            ) {

                items(movies) { // Lista de películas
                    if (searchText != "") { //Searching bar
                        if (searchText == it.title){
                            MovieItem(
                                id = it.id,
                                image = it.image_url,
                                title = it.title,
                                year = it.year,
                                rating = it.rating
                            )
                            {
                                navController.navigate("movieDetails/$it") // Navegar a detalles de la película
                            }
                        }
                    } else {
                        MovieItem(
                            id = it.id,
                            image = it.image_url,
                            title = it.title,
                            year = it.year,
                            rating = it.rating
                        )
                        {
                             navController.navigate("movieDetails/$it")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MovieItem(
    id: Int,
    image: String,
    title: String,
    year: Int,
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
                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(12.dp)),
                contentDescription = title)
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
                    text = "Año: $year",
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

@Preview(
    showBackground = true,
    showSystemUi = true
)

@Composable
fun HomeScreenPreview() {
    val sharedPref = SharedPref(context = LocalContext.current)

    ReviewsAppTheme {
        HomeScreen(
            innerPadding = PaddingValues(16.dp),
            navController = rememberNavController(),
            sharedPref = sharedPref
        )
    }
}

