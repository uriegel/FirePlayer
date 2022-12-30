package de.uriegel.fireplayer.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.ui.theme.Black

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// TODO Adapt FirePlayer dark theme
// TODO when in fire stick change theme (test)
// TODO Settings dialog

        setContent {
            FirePlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val systemUiController = rememberSystemUiController()
                    SideEffect {
                        systemUiController.setNavigationBarColor(
                            color = Black,
                            darkIcons = false
                        )
                    }
                    var displayMenu by remember { mutableStateOf(false) }
                    val context = LocalContext.current
                    Scaffold(topBar = {
                        TopAppBar(
                            title = { Text(getString(R.string.app_title) )},
                            actions = {
                                IconButton(onClick = { displayMenu = !displayMenu }) {
                                    Icon(Icons.Default.MoreVert, "Settings")
                                }
                                DropdownMenu(
                                    expanded = displayMenu,
                                    onDismissRequest = { displayMenu = false }
                                ) {

                                    DropdownMenuItem(onClick = {
                                        context.startActivity(Intent(context, SettingsActivity::class.java))
                                        displayMenu = false
                                    }) {
                                        Text(text = "Settings")
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

