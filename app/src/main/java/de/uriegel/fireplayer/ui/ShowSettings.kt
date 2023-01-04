package de.uriegel.fireplayer.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

@Composable
fun ShowSettings(navController: NavHostController) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        navController.navigate(NavRoutes.Init.route) { popUpTo(0) }
    }

    LaunchedEffect(Unit) {
        launcher.launch(Intent(context, SettingsActivity::class.java))
    }
}