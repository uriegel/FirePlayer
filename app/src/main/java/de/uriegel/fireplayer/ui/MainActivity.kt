package de.uriegel.fireplayer.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
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
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isTv =  android.os.Build.MODEL.contains("AFT")

        setContent {
            val context2 = LocalContext.current
            FirePlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .onKeyEvent {
                            Toast.makeText(context2, "Logout", Toast.LENGTH_LONG).show()
                            false
                        },
                    color = MaterialTheme.colors.background
                ) {
                    var displayMenu by remember { mutableStateOf(false) }
                    if (!isTv)
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
                                            showSettings()
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
                    else
                        MainScreen()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            showSettings()
        return super.onKeyDown(keyCode, event)
    }

    fun showSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}

