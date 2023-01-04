package de.uriegel.fireplayer.ui

sealed class NavRoutes(val route: String) {
    object MainScreen: NavRoutes("home")
    object Dialog: NavRoutes("dialog")
    object Dialog2: NavRoutes("dialog2")
    object ShowSettings: NavRoutes("showsettings")

    object ListItems: NavRoutes("listitems")
    //object Home: NavRoutes("homeorwhat")
    object Video: NavRoutes("video")
}