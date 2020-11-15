package de.uriegel.fireplayer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import kotlinx.android.synthetic.main.activity_web_player.*

class WebPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_player)

        val intent = intent
        film = intent.getStringExtra("film")!!

        with(webView) {
            with(settings) {
                javaScriptEnabled = true
                domStorageEnabled = true
                allowFileAccessFromFileURLs = true
                allowUniversalAccessFromFileURLs = true
                mediaPlaybackRequiresUserGesture = false
                setBackgroundColor(Color.BLACK)
            }
            //addJavascriptInterface(javaScriptInterface, "Native")
        }
        WebView.setWebContentsDebuggingEnabled(true)
        webView.loadUrl("file:///android_asset/index.html?video=https://uriegel.de/video/$film.mp4")
    }

    lateinit var film: String
}