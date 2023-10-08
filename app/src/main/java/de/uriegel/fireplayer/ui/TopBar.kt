package de.uriegel.fireplayer.ui

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import de.uriegel.fireplayer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTopBar(navController: NavHostController) {
    var displayMenu by remember { mutableStateOf(false) }

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
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.menu_settings)) },
                    onClick = {
                        Log.i("Firetag", "Menu settings")
                        navController.navigate(NavRoutes.ShowSettings.route)
                        displayMenu = false
                    })
            }
        }
    )
}