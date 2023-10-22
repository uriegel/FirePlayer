package de.uriegel.fireplayer.ui

import androidx.compose.runtime.*
import de.uriegel.fireplayer.extensions.fromBase64
import de.uriegel.fireplayer.extensions.getFilePath
import de.uriegel.fireplayer.extensions.isPicture
import de.uriegel.fireplayer.extensions.readAll
import de.uriegel.fireplayer.requests.getResponseStream
import de.uriegel.fireplayer.viewmodel.DirectoryItemsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PhotoScreen(position: Int,
                onPositionChanged: (Int)->Unit, viewModel: DirectoryItemsViewModel,
                path64: String?) {
    val path = path64?.fromBase64() ?: ""
    val filePath = path.getFilePath()
    val items = viewModel.items
        .filter { it.name.isPicture() }
        .map { (filePath + it.name).replace("+", "%20") }
    ImagePager(
        position,
        onPositionChanged,
        count = items.size,
        loadAsync = { loadBitmap(items[it]) }
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

