package de.uriegel.fireplayer.controller

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.exifinterface.media.ExifInterface
import de.uriegel.fireplayer.ui.MediaContent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ImagePagerController(
    count: Int,
    index: Int,
    nextFlow: SharedFlow<Boolean>,
    imageDataFlow: MutableSharedFlow<ImageData>,
    loadAsync: suspend (Int) -> MediaContent
) {
    LaunchedEffect(Unit) {
        var position = index
        var loadJob: Job? = null

        fun load(index: Int) {
            loadJob?.cancel()

            loadJob = launch {
                try {
                    val content = loadAsync(index)
                    val imageData = loadImageData(content)

                    imageDataFlow.emit(imageData)
                } catch (_: CancellationException) {
                    // The user navigated to another image.
                }
            }
        }

        // Load first image.
        load(position)

        nextFlow.collect { next ->

            val newPosition = if (next)
                position + 1
            else
                position - 1

            if (newPosition !in 0 until count)
                return@collect

            // IMPORTANT:
            // Update this BEFORE loading.
            position = newPosition

            // Cancel old load and immediately start the new one.
            load(newPosition)
        }
    }
}
private suspend fun loadImageData(content: MediaContent): ImageData =
    if (content.pictureBytes != null) {
        withContext(Dispatchers.IO) {
            val angle = content.pictureBytes.inputStream().use {
                val exif = ExifInterface(it)

                when (
                    exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                ) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                    ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                    ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                    else -> 0f
                }
            }

            ImageData(
                BitmapFactory.decodeByteArray(
                    content.pictureBytes,
                    0,
                    content.pictureBytes.size
                ),
                angle,
                null
            )
        }
    } else {
        ImageData(null, 0f, content.videoUrl)
    }

data class ImageData(
    val bitmap: Bitmap?,
    val angle: Float,
    val videoUrl: String?
)
