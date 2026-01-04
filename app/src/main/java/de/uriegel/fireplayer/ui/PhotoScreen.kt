//package de.uriegel.fireplayer.ui
//
//import androidx.compose.runtime.Composable
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//@Composable
//fun PhotoScreen(viewModel: DirectoryItemsViewModel, path64: String?) {
//    val path = path64?.fromBase64() ?: ""
//    val filePath = path.getFilePath()
//    val items = viewModel.items
//        .filter { it.name.isPicture() }
//        .map { (filePath + it.name).replace("+", "%20") }
//    ImagePager(
//        count = items.size,
//        loadAsync = { loadBitmap(items[it]) }
//    )
//}
//
//suspend fun loadBitmap(url: String): MediaContent =
//    if (url.endsWith(".mp4", true))
//        MediaContent(null, url)
//    else
//        withContext(Dispatchers.IO) {
//            return@withContext MediaContent(getResponseStream(url)
//                .map {
//                    it.readAll()
//                }
//                .getOrNull(), null)
//        }
//
//data class MediaContent(
//    val pictureBytes: ByteArray?,
//    val videoUrl: String?
//)