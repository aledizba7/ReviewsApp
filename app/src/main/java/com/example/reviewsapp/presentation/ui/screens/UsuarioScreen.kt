package com.example.reviewsapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reviewsapp.use_cases.SharedPref
import kotlinx.coroutines.launch
import com.example.reviewsapp.services.RetrofitInstance
import com.example.reviewsapp.presentation.ui.theme.Purple80
import com.example.reviewsapp.presentation.ui.theme.PurpleGrey80

@Composable
fun UsuarioScreen(navController: NavController, sharedPref: SharedPref) {
    // Estados para los datos de usuario y para el estado de carga/error
    var userEmail by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<Int?>(null) }
    var isLoading by remember { mutableStateOf(true) }  // Indicador de carga
    var errorMessage by remember { mutableStateOf<String?>(null) }  // Mensaje de error
    val scope = rememberCoroutineScope()

    // Obtener datos del usuario
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                // Obtener los datos del usuario usando el ID almacenado en SharedPreferences
                val userResponse = RetrofitInstance.api.getUserById(sharedPref.getUserIdSharedPref())

                // Asignar los valores recibidos a las variables
                userEmail = userResponse.email
                userId = userResponse.id

                // Actualizar el estado de carga
                isLoading = false

            } catch (e: Exception) {
                Log.e("UsuarioScreen", "Error al obtener usuario: ${e.message}")
                errorMessage = e.message  // Guardar el mensaje de error
                isLoading = false  // Finalizar carga incluso si hay error
            }
        }
    }

    // Diseño de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icono de usuario en el centro
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Icono de usuario",
                modifier = Modifier.size(120.dp),
                tint = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mostrar el estado de carga, datos de usuario o mensaje de error
            if (isLoading) {
                // Si está cargando, mostrar un indicador de carga
                CircularProgressIndicator()
            } else {
                // Si hay un error, mostrar el mensaje de error
                errorMessage?.let {
                    Text("Error: $it", color = Color.Red)  // Mostrar error en rojo
                } ?: run {
                    // Si no hay error, mostrar los datos del usuario
                    userEmail?.let {
                        Text("Correo: $it", color = Purple80, style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    userId?.let {
                        Text("ID: $it", color = PurpleGrey80, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
