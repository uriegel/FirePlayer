package de.uriegel.fireplayer.controller

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.exifinterface.media.ExifInterface
import de.uriegel.fireplayer.ui.MediaContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ImagePagerController(nextFlow: SharedFlow<Boolean>, imageDataFlow: MutableSharedFlow<ImageData>, loadAsync: suspend (Int)-> MediaContent) {
    val scope = rememberCoroutineScope()
    var position by remember { mutableIntStateOf(0) }
    //var imageDataNext: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}

    LaunchedEffect(Unit) {
        scope.launch {
            val imageData = loadImageData(loadAsync(position))
            imageDataFlow.emit(imageData)
      //      imageDataNext = loadImageData(loadAsync(1))
        }

        nextFlow.collect {
            scope.launch {
                val newPosition = if (it) position + 1 else position - 1
                val imageData = loadImageData(loadAsync(newPosition))
                position = newPosition
                imageDataFlow.emit(imageData)
            }
        }
    }
}

private suspend fun loadImageData(content: MediaContent): ImageData =
    if (content.pictureBytes != null)
        withContext(Dispatchers.IO) {
            val angle = content.pictureBytes.inputStream().use {
                val exif = ExifInterface(it)
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    else -> 0f
                }
            }
            return@withContext ImageData(BitmapFactory.decodeByteArray(content.pictureBytes, 0, content.pictureBytes.size), angle, null)
        } else
        ImageData(null, 0f, content.videoUrl)

data class ImageData(
    val bitmap: Bitmap?,
    val angle: Float,
    val videoUrl: String?
)
