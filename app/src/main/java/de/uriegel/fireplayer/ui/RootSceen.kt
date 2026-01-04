package de.uriegel.fireplayer.ui

import WebViewController
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun RootScreen(webController: WebViewController, webViewFocus: Boolean,
               onWelcome: (Boolean)->Unit, modifier: Modifier = Modifier) {
    var showPictures by remember { mutableStateOf(false) }

    Box(modifier.fillMaxSize()) {

        // 🔹 WebView is ALWAYS present
        ReactView(webController, webViewFocus, onWelcome,{ showPictures = true })

        // 🔹 Native overlay
        if (showPictures) {
           PicturesScreen(
               onBack = { showPictures = false }
           )
       }
    }

    // 🔙 Back handling
    BackHandler(enabled = showPictures) {
        showPictures = false
    }
}
