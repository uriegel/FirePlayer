package de.uriegel.fireplayer.ui

import WebViewController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlin.String

@Composable
fun RootScreen(webController: WebViewController, webViewFocus: Boolean,
               onWelcome: (Boolean)->Unit, modifier: Modifier = Modifier) {
    var showPictures by remember { mutableStateOf(false) }
    var baseUrl by remember { mutableStateOf("") }
    var items: Array<String> by remember { mutableStateOf(arrayOf<String>()) }
    var initialItemIndex by remember { mutableStateOf(0)}

    Box(modifier.fillMaxSize()) {

        // 🔹 WebView is ALWAYS present
        ReactView(webController, webViewFocus, onWelcome, webViewHasFocus = !showPictures, {
            url, imageItems, index ->
                run {
                    showPictures = true
                    baseUrl = url
                    items = imageItems
                    initialItemIndex = index
                }
        })

        // 🔹 Native overlay
        if (showPictures) {
            PhotoScreen(baseUrl, items, initialItemIndex, onBack = {
                    webController.send(WebCommand.SetFocusedImage(it))
                    showPictures = false
                }
            )
        }
    }
}
