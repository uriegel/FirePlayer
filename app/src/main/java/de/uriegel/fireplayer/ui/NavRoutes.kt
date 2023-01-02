package de.uriegel.fireplayer.ui

sealed class NavRoutes(val route: String) {
    object ListItems: NavRoutes("listitems")
    object Home: NavRoutes("home")
    object Video: NavRoutes("video")
}