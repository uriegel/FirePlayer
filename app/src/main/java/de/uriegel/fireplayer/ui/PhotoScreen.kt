package de.uriegel.fireplayer.ui

import androidx.compose.runtime.Composable
import de.uriegel.fireplayer.extensions.readAll
import de.uriegel.fireplayer.request.getResponseStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PhotoScreen(items: Array<String>, path: String) {
    val imageItems = items.map{ "$path/$it" }
    ImagePager(
        count = imageItems.size,
        loadAsync = { loadBitmap(imageItems[it]) }
    )
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