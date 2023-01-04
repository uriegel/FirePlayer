package de.uriegel.fireplayer.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import de.uriegel.fireplayer.android.ComponentExActivity
import de.uriegel.fireplayer.android.LifetimeTimer
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.exceptions.HttpProtocolException
import de.uriegel.fireplayer.exceptions.NotInitializedException
import de.uriegel.fireplayer.extensions.bind
import de.uriegel.fireplayer.requests.accessDisk
import de.uriegel.fireplayer.requests.initializeHttp
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class MainActivity : ComponentExActivity() {
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
                    val fullscreenMode = rememberSaveable { mutableStateOf(false) }
                    var stateText by rememberSaveable { mutableStateOf("") }
                    resetDisplayMode = { displayMode = DisplayMode.Default }

                    val coroutineScope = rememberCoroutineScope()
                    val context = LocalContext.current
                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(lifecycleOwner) {
                        val timer = LifetimeTimer(context, coroutineScope)
                        val observer = LifecycleEventObserver { _, event ->
                            when (event) {
                                Lifecycle.Event.ON_RESUME -> {
                                    coroutineScope.launch {
                                        if (displayMode != DisplayMode.Ok) {
                                            initializeHttp(this@MainActivity)
                                                .bind { accessDisk() }
                                                .fold(
                                                    {
                                                        displayMode = DisplayMode.Ok
                                                    },
                                                    {
                                                        when (it) {
                                                            is NotInitializedException -> showSettings()
                                                            is UnknownHostException -> {
                                                                stateText = it.localizedMessage ?: ""
                                                                displayMode = DisplayMode.UnknownHostError
                                                            }
                                                            is ConnectException -> {
                                                                stateText = it.localizedMessage ?: ""
                                                                displayMode = DisplayMode.ConnectError
                                                            }
                                                            is SSLException -> {
                                                                stateText = it.localizedMessage ?: ""
                                                                displayMode = DisplayMode.SslError
                                                            }
                                                            is HttpProtocolException -> {
                                                                stateText = "${it.code} ${it.localizedMessage}"
                                                                displayMode = DisplayMode.ProtocolError
                                                            }
                                                            else -> {
                                                                stateText = it.localizedMessage ?: ""
                                                                displayMode = DisplayMode.GeneralError
                                                            }
                                                        }
                                                    }
                                                )
                                        }
                                    }
                                }
                                Lifecycle.Event.ON_START -> timer.start()
                                Lifecycle.Event.ON_STOP -> timer.cancel()
                                else -> {}
                            }
                        }

                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                    @Composable
                    fun showContent(fullscreenMode: MutableState<Boolean>, padding: PaddingValues = PaddingValues()) {
                        when (displayMode) {
                            DisplayMode.Default -> StateDialog(R.string.initializing, padding = padding)
                            DisplayMode.Ok -> MainScreen(fullscreenMode)
                            DisplayMode.GeneralError -> StateDialog(R.string.general_error, stateText, padding = padding)
                            DisplayMode.ConnectError -> StateDialog(R.string.connect_error, stateText, padding = padding)
                            DisplayMode.UnknownHostError -> StateDialog(R.string.unknown_host_error, stateText, padding = padding)
                            DisplayMode.SslError -> StateDialog(R.string.ssl_error, stateText, padding = padding)
                            DisplayMode.ProtocolError -> StateDialog(R.string.protocol_error, stateText, padding = padding)
                        }
                    }

                    @Composable
                    fun createTopBar() =
                        AnimatedVisibility(
                            visible = !fullscreenMode.value,
                            enter = slideInVertically(initialOffsetY = { -it }),
                            exit = slideOutVertically(targetOffsetY = { -it }),
                            content = {
                                TopAppBar(
                                    title = { Text(getString(R.string.app_title)) },
                                    actions = {
                                        IconButton(onClick = { displayMenu = !displayMenu }) {
                                            Icon(
                                                Icons.Default.MoreVert,
                                                getString(R.string.menu_settings)
                                            )
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
                            }
                        )

                    if (!isTv)
                        Scaffold(
                            topBar = { createTopBar() },
                            content = { showContent(fullscreenMode, it) })
                    else
                        showContent(fullscreenMode)
                }
            }
        }
    }

    // TODO Modifier onKeyEvent
    // TODO showSettings without startActivity
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU)
            showSettings()
        return super.onKeyDown(keyCode, event)
    }

    private fun showSettings() {
        resetDisplayMode()
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private lateinit var resetDisplayMode: ()->Unit
}

enum class DisplayMode {
    Default,
    Ok,
    GeneralError,
    UnknownHostError,
    ConnectError,
    SslError,
    ProtocolError,
}