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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reviewsapp.presentation.ui.screens.LoginScreen
import com.example.reviewsapp.presentation.ui.screens.RegisterScreen
import com.example.reviewsapp.presentation.ui.screens.HomeScreen
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
                        startDestination =  if (isLogged) "home" else "login"
                    ){
                        composable(route = "login") {
                            LoginScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                        }
                        composable(route = "register") {
                            RegisterScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                        }
                        composable(route = "home") {
                            HomeScreen(
                                innerPadding = innerPadding,
                                navController = navController,
                                sharedPref = sharedPref
                            )
                        }
                    }
                }
            }
        }
    }
}

