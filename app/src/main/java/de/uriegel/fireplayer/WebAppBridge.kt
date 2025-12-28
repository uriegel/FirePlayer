package de.uriegel.fireplayer

import android.webkit.JavascriptInterface

class WebAppBridge(private val onAction: (String) -> Unit) {

    // Must be annotated with @JavascriptInterface
    @JavascriptInterface
    fun postMessage(message: String) {
        // This is called from JS
        onAction(message)
    }
}
