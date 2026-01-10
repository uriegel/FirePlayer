package de.uriegel.fireplayer

import android.app.Activity
import android.view.View
import android.webkit.JavascriptInterface
import androidx.annotation.Keep
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class WebAppBridge(private val activity: Activity, private val rootView: View,
                   private val onWelcome: (Boolean) -> Unit,
                   private val onOpenPictures: (baseUrl: String, items: Array<String>, index: Int) -> Unit,
                   private val onAction: (String) -> Unit) {
    @Keep
    @Suppress("unused")
    @JavascriptInterface
    fun postMessage(message: String) {
        onAction(message)
    }

    @Keep
    @Suppress("unused")
    @JavascriptInterface
    fun setWelcome(set: Boolean) {
        onWelcome(set)
    }

    @Keep
    @Suppress("unused")
    @JavascriptInterface
    fun enterFullscreen() {
        activity.runOnUiThread {
            val window = activity.window
            WindowCompat.setDecorFitsSystemWindows(window, false)

            WindowInsetsControllerCompat(window, rootView).apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    @Keep
    @Suppress("unused")
    @JavascriptInterface
    fun exitFullscreen() {
        activity.runOnUiThread {
            val window = activity.window
            WindowCompat.setDecorFitsSystemWindows(window, true)

            WindowInsetsControllerCompat(window, rootView)
                .show(WindowInsetsCompat.Type.systemBars())
        }
    }

    @Keep
    @Suppress("unused")
    @JavascriptInterface
    fun showPictures(baseUrl: String, items: Array<String>, index: Int) {
        activity.runOnUiThread {
            onOpenPictures(baseUrl, items, index)
        }
    }

    @Keep
    @Suppress("unused")
    @JavascriptInterface
    fun isTv() = de.uriegel.fireplayer.android.isTv()
}
