package com.example.reviewsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reviewsapp.presentation.ui.screens.LoginScreen
import com.example.reviewsapp.presentation.ui.screens.RegisterScreen
import com.example.reviewsapp.presentation.ui.screens.HomeScreen
import com.example.reviewsapp.presentation.ui.screens.MovieDetailScreen
import com.example.reviewsapp.presentation.ui.theme.ReviewsAppTheme
import com.example.reviewsapp.use_cases.SharedPref

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReviewsAppTheme {
                val navController = rememberNavController()
                val sharedPref = SharedPref(LocalContext.current)
                val isLogged = sharedPref.getIsLoggedSharedPref()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (isLogged) "home" else "login"
                    ) {
                        // Composable para la pantalla de Login
                        composable(route = "login") {
                            LoginScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                        }

                        // Composable para la pantalla de Registro
                        composable(route = "register") {
                            RegisterScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                        }

                        // Composable para la pantalla principal (Home)
                        composable(route = "home") {
                            HomeScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                        }

                        // Composable para la pantalla de detalles de una pelicula
                        composable(route = "movieDetails/{id}",
                            arguments = listOf(
                                navArgument("id") {
                                    type = NavType.IntType
                                    nullable = false
                                })) {
                            HomeScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                                val id = it.arguments?.getInt("id") ?: 0
                                MovieDetailScreen(id = id, innerPaddingValues = innerPadding, navController = navController)
                        }

                        // Composable para la pantalla de "Todas Películas"
                        composable("todasPeliculas") {
                            // Aquí iría la pantalla para "Todas Películas"
                            Text("Pantalla de Todas Películas")
                        }

                        // Composable para la pantalla de "Mis Reseñas"
                        composable("misPeliculas") {
                            // Aquí iría la pantalla para "Mis Reseñas"
                            Text("Pantalla de Mis Reseñas")
                        }

                        // Composable para la pantalla de "Usuario"
                        composable("usuario") {
                            // Aquí iría la pantalla para "Usuario"
                            Text("Pantalla de Usuario")
                        }
                    }
                }
            }
        }
    }
}
