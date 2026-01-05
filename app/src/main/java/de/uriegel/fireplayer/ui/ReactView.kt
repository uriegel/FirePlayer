package de.uriegel.fireplayer.ui

import WebViewController
import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import de.uriegel.fireplayer.WebAppBridge

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ReactView(webController: WebViewController, webViewFocus: Boolean, onWelcome: (Boolean)->Unit,
              webViewHasFocus: Boolean, onOpenPictures: (baseUrl: String, items: Array<String>) -> Unit) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val view = LocalView.current
    if (activity == null)
        return

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            isFocusable = webViewFocus
            isFocusableInTouchMode = webViewFocus

            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true

            post { if (webViewFocus) requestFocus() }

            settings.mediaPlaybackRequiresUserGesture = false
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            clearCache(true)

            addJavascriptInterface(WebAppBridge(activity, view, onWelcome,
                onOpenPictures) {
                message ->
                    Toast
                        .makeText(this.context, message, Toast.LENGTH_SHORT)
                        .show()
            }, "AndroidBridge")

            loadUrl("http://127.0.0.1:8888/")
            //loadUrl("http://192.168.178.36:5173/")
        }
    }

    LaunchedEffect(Unit) {
        webController.events.collect { command ->
            when (command) {
                WebCommand.BackPressed -> webView.evaluateJavascript("window.onBackPressed()", null)
                WebCommand.FastRewind -> webView.evaluateJavascript("window.onFastRewind()", null)
                WebCommand.FastForward -> webView.evaluateJavascript("window.onFastForward()", null)
                WebCommand.Stop -> webView.evaluateJavascript("window.onStop()", null)
                WebCommand.Play -> webView.evaluateJavascript("window.onPlay()", null)
                WebCommand.Pause -> webView.evaluateJavascript("window.onPause()", null)
                WebCommand.Left -> webView.evaluateJavascript("window.onLeft()", null)
                WebCommand.Right -> webView.evaluateJavascript("window.onRight()", null)
            }
        }
    }

    LaunchedEffect(webViewHasFocus) {
        webView.isFocusable = webViewHasFocus
        webView.isFocusableInTouchMode = webViewHasFocus

        if (webViewHasFocus) {
            webView.requestFocus()
        } else {
            webView.clearFocus()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webView },
    )
}