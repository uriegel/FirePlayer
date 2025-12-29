package de.uriegel.fireplayer.ui

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.widget.Toast
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

/*
 TODO
 * Test custom buttons

 * useWideViewPort
 * loadWithOverviewMode

 * Layout
                        =
    Filme  Bilder   Musik

 * =: Options

 * rotate on smart phone

 * Navigate to new page: Settings

    url

    Sony Bravia (later)

 * Video screen displaying video folders and videos
 */


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        server = WebServer(this, 8888)
        server.start()

        var welcomePage = false

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sendToWeb("BACK pressed")
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
            KeyEvent.KEYCODE_DPAD_LEFT ->
                sendToWeb("LEFT")

            KeyEvent.KEYCODE_DPAD_RIGHT ->
                sendToWeb("RIGHT")

            KeyEvent.KEYCODE_DPAD_UP ->
                sendToWeb("UP")

            KeyEvent.KEYCODE_DPAD_DOWN ->
                sendToWeb("DOWN")

            KeyEvent.KEYCODE_DPAD_CENTER ->
                sendToWeb("SELECT")

            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE ->
                sendToWeb("PLAY_PAUSE")

            KeyEvent.KEYCODE_MEDIA_PLAY ->
                sendToWeb("PLAY")

            KeyEvent.KEYCODE_MEDIA_PAUSE ->
                sendToWeb("PAUSE")

            KeyEvent.KEYCODE_MEDIA_STOP ->
                sendToWeb("STOP")

            KeyEvent.KEYCODE_MEDIA_REWIND ->
                sendToWeb("REWIND")

            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD ->
                sendToWeb("FAST_FORWARD")

            else ->
                sendToWeb("Sonstige: ${event.keyCode}")
        }

        return super.dispatchKeyEvent(event)
    }

    private fun sendToWeb(action: String) {
        Toast.makeText(this, action, Toast.LENGTH_SHORT).show()
//        webView?.evaluateJavascript(
//            "window.onFireTvRemote && window.onFireTvRemote('$action');",
//            null
//        )
    }

    private fun sendBackPressed() {
        webView?.evaluateJavascript("window.onBackPressed()", null)
    }

    private lateinit var server: WebServer
    private var webView: WebView? = null
}

