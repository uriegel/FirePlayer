package de.uriegel.fireplayer.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import de.uriegel.fireplayer.extensions.fromBase64

@Composable
fun VideoScreen(navController: NavHostController, path64: String?) {
    val context = LocalContext.current
    val path = path64!!.fromBase64()
    Text(text = path)
}
