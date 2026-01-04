package de.uriegel.fireplayer.ui

import WebViewController
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.uriegel.fireplayer.WebServer
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.extensions.ComponentExActivity

class MainActivity : ComponentExActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        server = WebServer(this, 8888)
        server.start()

        var welcomePage = false

        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (welcomePage)
                    finish()
                else
                    webController.send(WebCommand.BackPressed)
            }
        })

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.BLACK
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.BLACK
            )
        )

        val webViewFocus = true

        setContent {
            FirePlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RootScreen(webController, webViewFocus, { set -> welcomePage = set },
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        server.stop()
        super.onDestroy()
    }

    @Suppress("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action != KeyEvent.ACTION_DOWN)
            return super.dispatchKeyEvent(event)

        when (event.keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> webController.send(WebCommand.Left)
            KeyEvent.KEYCODE_DPAD_RIGHT -> webController.send(WebCommand.Right)
            KeyEvent.KEYCODE_MEDIA_PLAY -> webController.send(WebCommand.Play)
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> webController.send(WebCommand.Pause)
            KeyEvent.KEYCODE_MEDIA_PAUSE -> webController.send(WebCommand.Pause)
            KeyEvent.KEYCODE_MEDIA_STOP -> webController.send(WebCommand.Stop)
            KeyEvent.KEYCODE_MEDIA_REWIND -> webController.send(WebCommand.FastRewind)
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> webController.send(WebCommand.FastForward)
//            KeyEvent.KEYCODE_DPAD_UP -> sendToWeb("UP")
//            KeyEvent.KEYCODE_DPAD_DOWN -> sendToWeb("DOWN")
//            KeyEvent.KEYCODE_DPAD_CENTER -> sendToWeb("SELECT")
        }

        return super.dispatchKeyEvent(event)
    }

    private lateinit var server: WebServer
    private val webController: WebViewController by viewModels()
}

@Composable
fun PicturesScreen(baseUrl: String, items: Array<String>, onBack: ()->Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black), contentAlignment = Alignment.Center) {
        //Text(text = "Welcome to Compose Settings Page!", color = Color.Red)
        PhotoScreen(items, baseUrl)
    }
}