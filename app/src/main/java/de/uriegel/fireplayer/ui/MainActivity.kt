package de.uriegel.fireplayer.ui

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import de.uriegel.fireplayer.WebServer
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme

class MainActivity : ComponentActivity() {
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
                    sendBackPressed()
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
                    ReactView(webViewFocus, { webView = it },
                        { set -> welcomePage = set },
                        Modifier.padding(innerPadding))
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
            KeyEvent.KEYCODE_DPAD_LEFT -> sendLeft()
            KeyEvent.KEYCODE_DPAD_RIGHT -> sendRight()
            KeyEvent.KEYCODE_MEDIA_PLAY -> sendPlay()
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> sendPause()
            KeyEvent.KEYCODE_MEDIA_PAUSE -> sendPause()
            KeyEvent.KEYCODE_MEDIA_STOP -> sendStop()
            KeyEvent.KEYCODE_MEDIA_REWIND -> sendFastRewind()
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> sendFastForward()
//            KeyEvent.KEYCODE_DPAD_UP -> sendToWeb("UP")
//            KeyEvent.KEYCODE_DPAD_DOWN -> sendToWeb("DOWN")
//            KeyEvent.KEYCODE_DPAD_CENTER -> sendToWeb("SELECT")
        }

        return super.dispatchKeyEvent(event)
    }

    private fun sendBackPressed() {
        webView?.evaluateJavascript("window.onBackPressed()", null)
    }

    private fun sendPlay() {
        webView?.evaluateJavascript("window.onPlay()", null)
    }

    private fun sendPause() {
        webView?.evaluateJavascript("window.onPause()", null)
    }

    private fun sendStop() {
        webView?.evaluateJavascript("window.onStop()", null)
    }

    private fun sendRight() {
        webView?.evaluateJavascript("window.onRight()", null)
    }

    private fun sendLeft() {
        webView?.evaluateJavascript("window.onLeft()", null)
    }

    private fun sendFastForward() {
        webView?.evaluateJavascript("window.onFastForward()", null)
    }

    private fun sendFastRewind() {
        webView?.evaluateJavascript("window.onFastRewind()", null)
    }

    private lateinit var server: WebServer
    private var webView: WebView? = null
}

