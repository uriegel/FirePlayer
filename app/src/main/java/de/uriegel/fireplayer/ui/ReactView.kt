package de.uriegel.fireplayer.ui

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ReactView(onWebViewReady: (WebView)->Unit, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                settings.javaScriptCanOpenWindowsAutomatically = true


                settings.mediaPlaybackRequiresUserGesture = false
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                clearCache(true)

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