package de.uriegel.fireplayer.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.exceptions.HttpProtocolException
import de.uriegel.fireplayer.exceptions.NotInitializedException
import de.uriegel.fireplayer.extensions.bind
import de.uriegel.fireplayer.requests.accessDisk
import de.uriegel.fireplayer.requests.initializeHttp
import kotlinx.coroutines.launch

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
                    var displayMode by rememberSaveable { mutableStateOf(DisplayMode.Default) }
                    resetDisplayMode = { displayMode = DisplayMode.Default }

                    val coroutineScope = rememberCoroutineScope()
                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                coroutineScope.launch {
                                    if (displayMode != DisplayMode.Ok) {
                                        initializeHttp(this@MainActivity)
                                            .bind { accessDisk() }
                                            .fold(
                                                {
                                                    urlParts = arrayOf("/video")
                                                    displayMode = DisplayMode.Ok
                                                },
                                                {
                                                    when (it) {
                                                        // TODO not always refreshed
                                                        is NotInitializedException -> showSettings()
                                                        is HttpProtocolException -> displayMode = DisplayMode.Error
                                                        else -> displayMode = DisplayMode.Error
                                                        // TODO if no connection screen with text check connection and one button "settings"
                                                    }
                                                }
                                            )
                                    }
                                }
                            }
                        }

                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                    @Composable
                    fun showContent(padding: PaddingValues = PaddingValues()) {
                        when (displayMode) {
                            DisplayMode.Default -> Text("Initialisieren...")
                            DisplayMode.Ok -> MainScreen(padding)
                            DisplayMode.Error -> Text("Nich gut")
                        }
                    }

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
                        }, content = { showContent(it) })
                    else
                        showContent()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            showSettings()
        return super.onKeyDown(keyCode, event)
    }

    private fun showSettings() {
        resetDisplayMode()
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private var urlParts = arrayOf<String>()
    private lateinit var resetDisplayMode: ()->Unit
}

enum class DisplayMode {
    Default,
    Ok,
    Error
}