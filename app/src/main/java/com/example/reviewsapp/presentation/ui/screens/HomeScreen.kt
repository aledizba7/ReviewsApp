package com.example.reviewsapp.presentation.ui.screens

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.reviewsapp.presentation.ui.theme.ReviewsAppTheme
import com.example.reviewsapp.presentation.ui.utils.Logout
import com.example.reviewsapp.use_cases.SharedPref

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavHostController,
    sharedPref: SharedPref
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Hola Mundo",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
            IconButton(
                onClick = {
                    sharedPref.removeUserSharedPref()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(imageVector = Logout, contentDescription = "Logout" )
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
    ReviewsAppTheme {
        HomeScreen(
            innerPadding = PaddingValues(0.dp),
            navController = rememberNavController(),
            sharedPref = SharedPref(LocalContext.current)
        )
    }
}