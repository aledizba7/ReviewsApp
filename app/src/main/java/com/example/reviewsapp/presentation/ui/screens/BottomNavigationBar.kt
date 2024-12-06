package com.example.reviewsapp.presentation.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState


data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", "Inicio", Icons.Filled.Home),
        BottomNavItem("todasPeliculas", "Todas", Icons.Filled.Movie),
        BottomNavItem("misPeliculas", "Crear Peli", Icons.Filled.Favorite),
        BottomNavItem("usuario", "Usuario", Icons.Filled.Person),
    )

    NavigationBar(containerColor = Color(0xFF1B1B1B)) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(text = item.title, color = Color.White)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFE50914), // Color de ícono seleccionado
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}
