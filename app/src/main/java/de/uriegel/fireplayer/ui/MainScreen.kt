package de.uriegel.fireplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.uriegel.fireplayer.extensions.toBase64

@Composable
fun MainScreen(fullscreenMode: MutableState<Boolean>) {
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

        composable(NavRoutes.Video.route + "/{path}") {
            val path = it.arguments?.getString("path")
            VideoScreen(fullscreenMode, path)
        }
    }
}