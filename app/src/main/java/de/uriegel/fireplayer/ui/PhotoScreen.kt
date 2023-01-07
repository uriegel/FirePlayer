package de.uriegel.fireplayer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import de.uriegel.fireplayer.extensions.fromBase64
import de.uriegel.fireplayer.extensions.getFilePath
import de.uriegel.fireplayer.extensions.isPicture
import de.uriegel.fireplayer.requests.getBaseUrl
import de.uriegel.fireplayer.viewmodel.DirectoryItemsViewModel
import java.net.URL

@Composable
fun PhotoScreen(viewModel: DirectoryItemsViewModel, path64: String?) {
    val path = path64?.fromBase64() ?: ""
    val filePath = path.getFilePath()


    val photos = viewModel
        .items
        .filter { it.name.isPicture() }
        .map { getBaseUrl() + (filePath + it.name).replace("+", "%20") }

    var photoIndex by remember { mutableStateOf(0) }
    var url by remember { mutableStateOf(photos[photoIndex]) }
    Box(Modifier.fillMaxSize()) {
        GlideImage(
            modifier = Modifier.fillMaxSize(),
            imageModel = { URL(url) },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
        )
        Button(onClick = { url = photos[photoIndex++] }) {
            Text(text = ">")
        }
    }
}