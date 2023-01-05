package de.uriegel.fireplayer.ui

sealed class NavRoutes(val route: String) {
    object Init: NavRoutes("init")
    object Dialog: NavRoutes("dialog")
    object Dialog2: NavRoutes("dialog2")
    object ShowSettings: NavRoutes("showsettings")
    object Folders: NavRoutes("folders")
    object ItemsRoot: NavRoutes("itemsroot")
    object Items: NavRoutes("items")
    object Video: NavRoutes("video")
}