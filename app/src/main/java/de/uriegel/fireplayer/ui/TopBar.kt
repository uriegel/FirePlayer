package de.uriegel.fireplayer.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import de.uriegel.fireplayer.R

@Composable
fun createTopBar(navController: NavHostController, fullscreenMode: MutableState<Boolean>) {
    var displayMenu by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = !fullscreenMode.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_title)) },
                actions = {
                    IconButton(onClick = { displayMenu = !displayMenu }) {
                        Icon(
                            Icons.Default.MoreVert,
                            stringResource(R.string.menu_settings)
                        )
                    }
                    DropdownMenu(
                        expanded = displayMenu,
                        onDismissRequest = { displayMenu = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            Log.i("Firetag", "Menu settings")
                            navController.navigate(NavRoutes.ShowSettings.route)
                            displayMenu = false
                        }) {
                            Text(text = stringResource(R.string.menu_settings))
                        }
                    }
                }
            )
        }
    )
}