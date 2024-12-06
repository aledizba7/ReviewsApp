package com.example.reviewsapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.reviewsapp.presentation.ui.theme.PurpleGrey40
import com.example.reviewsapp.presentation.ui.theme.PurpleGrey80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioScreen(navController: NavController, sharedPref: SharedPref, innerPadding: PaddingValues,) {
    // Estados para los datos de usuario y para el estado de carga/error
    var userEmail by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<Int?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
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
                errorMessage = e.message
                isLoading = false
            }
        }
    }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text( //Revisar si en otros celulares se alcanza a ver el texto completo. (en el pequeño no)
                    text = "Detalles de Usuario",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )},
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
            )},
            bottomBar = { BottomNavigationBar(navController) },
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
            LazyColumn(contentPadding = innerPadding) {  } // Para que el LazyColumn no de error.
            // Diseño de la pantalla
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    // Icono de usuario
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Icono de usuario",
                        modifier = Modifier.size(150.dp),
                        tint = Purple80
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Mostrar el estado de carga, datos de usuario o mensaje de error
                    if (isLoading) {
                        CircularProgressIndicator(color = Purple80)
                    } else {
                        errorMessage?.let {
                            Text("Error: $it", color = Color.Red, style = MaterialTheme.typography.titleMedium)
                        } ?: run {
                            userEmail?.let {
                                Text("Correo: $it", color = PurpleGrey40, style = MaterialTheme.typography.titleMedium)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            userId?.let {
                                Text("ID: $it", color = PurpleGrey80, style = MaterialTheme.typography.titleMedium)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    navController.navigate("login") {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                Text("Cerrar sesión", color = Color.White, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }

    }
}