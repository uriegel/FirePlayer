package de.uriegel.fireplayer.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.exifinterface.media.ExifInterface
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.android.isTv
import de.uriegel.fireplayer.extensions.fromBase64
import de.uriegel.fireplayer.extensions.getFilePath
import de.uriegel.fireplayer.extensions.isPicture
import de.uriegel.fireplayer.extensions.readAll
import de.uriegel.fireplayer.requests.getResponseStream
import de.uriegel.fireplayer.viewmodel.DirectoryItemsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@Composable
fun PhotoScreen(viewModel: DirectoryItemsViewModel, path64: String?) {
    val path = path64?.fromBase64() ?: ""
    val filePath = path.getFilePath()
    val items = viewModel.items
        .filter { it.name.isPicture() }
        .map { (filePath + it.name).replace("+", "%20") }
    if (isTv())
        ImageCrossFadePager(
            count = items.size,
            loadAsync = { loadBitmap(items[it]) }
        )
    else
        ImagePager(
            count = items.size,
            loadAsync = { loadBitmap(items[it]) }
        )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImagePager(
    count: Int,
    loadAsync: suspend (Int)-> ImageData?
) {
    val pagerState = rememberPagerState()
    HorizontalPager(count = count, state = pagerState) { pager ->
        Box(Modifier
            .fillMaxSize()) {
            AsyncImage(
                modifier = Modifier.align(Alignment.Center),
                contentDescription = "Image",
                loadAsync = { loadAsync(pager) }
            )
        }
    }
}

@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    loadAsync: suspend ()-> ImageData?,
    contentDescription: String,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var angle by remember { mutableFloatStateOf(0f)}
    val bitmap = remember {
        mutableStateOf(BitmapFactory.decodeResource(context.resources, R.drawable.emptypics))
    }

    LaunchedEffect(true) {
        scope.launch {
            loadAsync()?.apply {
                angle = this.angle
                bitmap.value = this.bitmap
            }
        }
    }

    Image(
        modifier = modifier.rotate(angle),
        bitmap = bitmap.value.asImageBitmap(),
        contentDescription = contentDescription,
    )
}

suspend fun loadBitmap(url: String): ImageData? =
    withContext(Dispatchers.IO) {
        return@withContext getResponseStream(url)
            .map {
                it.readAll()
            }
            .fold({
                val angle = it.inputStream().use { bytes ->
                    val exif = ExifInterface(bytes)
                    val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                        90f
                    else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                        270f
                    else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                        180f
                    else
                        0f
                }
                ImageData(BitmapFactory.decodeByteArray(it, 0, it.size), angle)
            }, { null })
    }

data class ImageData(
    val bitmap: Bitmap,
    val angle: Float
)