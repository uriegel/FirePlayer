package de.uriegel.fireplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import de.uriegel.fireplayer.extensions.readAll
import de.uriegel.fireplayer.request.getResponseStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun PhotoScreen(path: String, items: Array<String>, index: Int, onBack: (index: Int)->Unit) {
    val focusRequester = remember { FocusRequester() }
    val imageItems = items.map{ "$path/$it" }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .focusRequester(focusRequester)
        .focusable(), contentAlignment = Alignment.Center) {
            ImagePager(
                count = imageItems.size, index,
                loadAsync = { loadBitmap(imageItems[it]) }, onBack)
       }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

suspend fun loadBitmap(url: String): MediaContent =
    if (url.endsWith(".mp4", true))
        MediaContent(null, url)
    else
        withContext(Dispatchers.IO) {
            return@withContext MediaContent(getResponseStream(url)
                .map {
                    it.readAll()
                }
                .getOrNull(), null)
        }

data class MediaContent(
    val pictureBytes: ByteArray?,
    val videoUrl: String?
)