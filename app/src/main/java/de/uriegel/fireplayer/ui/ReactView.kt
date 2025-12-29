package de.uriegel.fireplayer.ui

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import de.uriegel.fireplayer.WebAppBridge

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ReactView(webViewFocus: Boolean, onWebViewReady: (WebView)->Unit,
              onWelcome: (Boolean)->Unit, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                isFocusable = webViewFocus
                isFocusableInTouchMode = webViewFocus

                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true

//                post { if (webViewFocus) requestFocus() }

                settings.mediaPlaybackRequiresUserGesture = false
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                clearCache(true)

                addJavascriptInterface(WebAppBridge(onWelcome, { message ->
                    // Handle messages from JS
                    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
                }), "AndroidBridge")

                loadUrl("http://127.0.0.1:8888/")
                onWebViewReady(this)
//                post {
//                    evaluateJavascript(
//                        "window.dispatchEvent(new Event('resize'))",
//                        null
//                    )
//                }
            }
        },
//        update = { webView ->
//            // You can update WebView state here if needed
//        }
    )
}