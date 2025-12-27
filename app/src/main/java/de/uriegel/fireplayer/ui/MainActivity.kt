package de.uriegel.fireplayer.ui

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme

/*
 TODO
 * HTTP Server
 * Lifecycle rotate
 * Insert React app
 * Layout
                        =
    Filme  Bilder   Musik

 * =: Options

 * Navigate to new page: Settings

    url

    Sony Bravia (later)
 */


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FirePlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
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

            KeyEvent.KEYCODE_BACK ->
                sendToWeb("BACK")

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
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
