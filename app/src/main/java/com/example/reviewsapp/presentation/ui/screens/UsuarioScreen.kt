package com.example.reviewsapp.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reviewsapp.use_cases.SharedPref
import kotlinx.coroutines.launch

// Define una clase de datos para representar a los usuarios
data class Users(val email: String)

@Composable
fun UsuarioScreen(navController: NavController, sharedPref: SharedPref) {
    // Variable de estado para almacenar la información del usuario
    var users by remember { mutableStateOf<Users?>(null) }
    val scope = rememberCoroutineScope()

    // LaunchedEffect para obtener la información del usuario al iniciar la pantalla
    LaunchedEffect(Unit) {
        scope.launch {
            users = getUsersFromApi() // Reemplaza con tu lógica para obtener users de la API
        }
    }

    // Fondo oscuro con un Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray) // Color de fondo oscuro
    ) {
        // Diseño de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Mostrar el correo registrado si está disponible
            users?.let {
                Text("Correo: ${it.email}", color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botones para cambiar contraseña, usuario, correo y cerrar sesión
            Button(onClick = { /* Acción para cambiar contraseña */ }) {
                Text("Cambiar contraseña", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { /* Acción para cambiar usuario */ }) {
                Text("Cambiar usuario", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { /* Acción para cambiar correo */ }) {
                Text("Cambiar correo", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            ) {
                Text("Cerrar sesión", color = Color.White)
            }
        }
    }
}

// Función de ejemplo para obtener la información del usuario de la API
suspend fun getUsersFromApi(): Users? {
    return Users(email = "example@example.com") // Devuelve un objeto Users con el correo
}
