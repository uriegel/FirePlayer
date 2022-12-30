package de.uriegel.fireplayer.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.preference.PreferenceManager
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isTv =  android.os.Build.MODEL.contains("AFT")

        setContent {
            FirePlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
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

    override fun onResume() {
        super.onResume()

        if (urlParts.isEmpty()) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
            preferences.getString("url", "")?.let {
                if (it.length  >= 6) {
                    urlParts = arrayOf("${it}/video")
            }
                //basicAuthentication(preferences.getString("name", "")!!, preferences.getString("auth_pw", "")!!)

//                activityRequest.launch(Intent(this@MainActivity, SettingsActivity::class.java))
//                url = preferences.getString("url", "")
            }

            //MainActivity.url = url!!
        }
        if (urlParts.isEmpty())
            showSettings()
        // TODO if no connection screen with text check connection and one button "settings"
        // TODO if connection and response error "settings"
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            showSettings()
        return super.onKeyDown(keyCode, event)
    }

    private fun showSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private var urlParts = arrayOf<String>()
}

