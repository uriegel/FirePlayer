package de.uriegel.fireplayer.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.exifinterface.media.ExifInterface
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.extensions.onKeyDown
import de.uriegel.fireplayer.extensions.toBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val tween = 2000

@Composable
fun ImagePager(
    count: Int,
    loadAsync: suspend (Int)-> MediaContent
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var secondVisible by remember { mutableStateOf(false)}
    var index by remember { mutableIntStateOf(0) }
    var loading by remember { mutableStateOf(false)}
    var imageData1: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}
    var imageData2: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}
    var imageDataNext: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}
    var imageDataPrev: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}

    LaunchedEffect(true) {
        scope.launch {
            imageData1 = loadImageData(loadAsync(0))
            imageDataNext = loadImageData(loadAsync(1))
        }
    }

    fun next() {
        if (!loading && index < count - 1) {
            if (secondVisible) {
                imageData1 = imageDataNext
                imageDataPrev = imageData2
            } else {
                imageData2 = imageDataNext
                imageDataPrev = imageData1
            }
            loading = true
            secondVisible = !secondVisible
            scope.launch {
                if (index++ < count - 2)
                    imageDataNext = loadImageData(loadAsync(index + 1))
                loading = false
            }
        }
    }

    fun previous() {
        if (!loading && index != 0) {
            if (secondVisible) {
                imageData1 = imageDataPrev
                imageDataNext = imageData2
            } else {
                imageData2 = imageDataPrev
                imageDataNext = imageData1
            }
            loading = true
            secondVisible = !secondVisible
            scope.launch {
                if (index-- > 1)
                    imageDataPrev = loadImageData(loadAsync(index - 1))
                loading = false
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .draggable(
            orientation = Orientation.Horizontal,
            state = rememberDraggableState {
                if (it > -20 && it < 0)
                    next()
                else if (it < 20 && it > 0)
                    previous()
            }
        )
        .onKeyDown(context) { _, evt ->
            when (evt?.keyCode) {
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    next()
                    true
                }

                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    previous()
                    true
                }

                else -> false
            }
        }
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Center),
            visible = !secondVisible,
            enter = fadeIn(
                tween(tween)
            ),
            exit = fadeOut(
                tween(tween)
            )
        ) {
            MediaContent(imageData1, context)
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.Center),
            visible = secondVisible,
            enter = fadeIn(
                tween(tween)
            ),
            exit = fadeOut(
                tween(tween)
            )
        ) {
            MediaContent(imageData2, context)
        }
    }
}

@Composable
private fun MediaContent(imageData: ImageData, context: Context) {
    Box() {
        if (imageData.bitmap != null)
            RotatableImage(imageData, context)
        else if (imageData.videoUrl != null)
            VideoScreen(imageData.videoUrl.toBase64())
        else
            Text(text = "")
    }
}


@Composable
private fun RotatableImage(imageData: ImageData?, context: Context, modifier: Modifier = Modifier) =
    Image(
        modifier = modifier
            .then(
                if (imageData?.angle != 0f) {
                    Modifier
                        .rotate(imageData?.angle ?: 0f)
                        .scale(
                            (imageData?.bitmap?.height?.toFloat()
                                ?: 1f) / (imageData?.bitmap?.width?.toFloat() ?: 1f)
                        )
                } else
                    Modifier
            ),
        bitmap = imageData?.bitmap?.asImageBitmap()
            ?: BitmapFactory.decodeResource(context.resources,
                R.drawable.emptypics).asImageBitmap(),
        contentDescription = "Image",
    )

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

private data class ImageData(
    val bitmap: Bitmap?,
    val angle: Float,
    val videoUrl: String?
)
