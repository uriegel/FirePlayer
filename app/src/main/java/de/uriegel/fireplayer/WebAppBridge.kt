package de.uriegel.fireplayer

import android.webkit.JavascriptInterface

class WebAppBridge(private val onWelcome: (Boolean) -> Unit, private val onAction: (String) -> Unit) {
    @JavascriptInterface
    fun postMessage(message: String) {
        onAction(message)
    }
    @JavascriptInterface
    fun setWelcome(set: Boolean) {
        onWelcome(set)
    }
}
