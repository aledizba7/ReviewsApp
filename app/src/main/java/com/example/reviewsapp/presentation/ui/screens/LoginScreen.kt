package com.example.reviewsapp.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.reviewsapp.R
import com.example.reviewsapp.services.LoginService
import com.example.reviewsapp.dtos.AuthDto
import com.example.reviewsapp.use_cases.SharedPref
import com.example.reviewsapp.presentation.ui.theme.ReviewsAppTheme
import com.example.reviewsapp.presentation.ui.utils.Lock
import com.example.reviewsapp.presentation.ui.utils.Visibility
import com.example.reviewsapp.presentation.ui.utils.Visibility_off
import com.example.reviewsapp.presentation.ui.theme.ReviewsAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun LoginScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref
) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.movies_login),
                contentDescription = "login",
                modifier = Modifier.size(190.dp)
            )
            Spacer(modifier = Modifier.height(35.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                placeholder = { Text(text = "Correo Electrónico") },
                leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "email") }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                placeholder = { Text(text = "Contraseña") },
                leadingIcon = { Icon(imageVector = Lock, contentDescription = "email") },
                visualTransformation =
                if (!isPasswordVisible) PasswordVisualTransformation()
                else VisualTransformation.None,
                trailingIcon = {
                    IconButton(onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                        val icon = if(!isPasswordVisible) Visibility_off else Visibility
                        Icon(imageVector = icon, contentDescription = "hide")
                    }
                }

            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            val authService = Retrofit.Builder()
                                .baseUrl("http://10.0.2.2:8000/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                                .create(LoginService::class.java)
                            val loginDto = AuthDto(email = email, password = password)
                            val response = authService.login(loginDto)
                            Log.i("LoginScreenAPI", response.toString())
                            if (response.code() == 200) {
                                if (response.body()?.isLogged == true) {
                                    withContext(Dispatchers.Main) {
                                        sharedPref.saveUserSharedPref(
                                            userId = response.body()?.userId ?: 0 ,
                                            isLogged = response.body()?.isLogged ?: false
                                        )
                                        navController.navigate("home"){
                                            popUpTo("home") { inclusive = true  }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Iniciar Sesión", fontSize = 17.sp)
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Crear una cuenta",
                color = Color.Gray,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenPreview() {
    ReviewsAppTheme {
        LoginScreen(
            innerPadding = PaddingValues(0.dp),
            navController = rememberNavController(),
            sharedPref = SharedPref(LocalContext.current)
        )
    }
}