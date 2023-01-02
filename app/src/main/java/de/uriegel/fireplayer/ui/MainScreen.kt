package de.uriegel.fireplayer.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.uriegel.fireplayer.extensions.toBase64

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
    ) {
        composable(NavRoutes.Home.route) {
            ItemsScreen(navController = navController, "/video".toBase64())
        }

        composable(NavRoutes.ListItems.route + "/{path}") {
            val path = it.arguments?.getString("path")
            ItemsScreen(navController = navController, path)
        }
    }
}