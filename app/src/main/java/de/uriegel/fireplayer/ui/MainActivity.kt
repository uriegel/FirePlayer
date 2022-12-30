package de.uriegel.fireplayer.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FirePlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var displayMenu by remember { mutableStateOf(false) }
                    val context = LocalContext.current
                    Scaffold(topBar = {
                        TopAppBar(
                            title = { Text(getString(R.string.app_title) )},
                            actions = {
                                IconButton(onClick = { displayMenu = !displayMenu }) {
                                    Icon(Icons.Default.MoreVert, getString(R.string.menu_settings))
                                }
                                DropdownMenu(
                                    expanded = displayMenu,
                                    onDismissRequest = { displayMenu = false }
                                ) {

                                    DropdownMenuItem(onClick = {
                                        context.startActivity(Intent(context, SettingsActivity::class.java))
                                        displayMenu = false
                                    }) {
                                        Text(text = getString(R.string.menu_settings))
                                    }
                                }
                            }
                        )
                    }, content = {
                        MainScreen(it)
                    })
                }
            }
        }
    }
}

