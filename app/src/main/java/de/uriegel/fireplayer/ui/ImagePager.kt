package de.uriegel.fireplayer.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.KeyEvent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.exifinterface.media.ExifInterface
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.extensions.onKeyDown
import kotlinx.coroutines.launch

@Composable
fun ImagePager(
    count: Int,
    loadAsync: suspend (Int)-> ByteArray?,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var index by remember { mutableIntStateOf(0) }
    var loading by remember { mutableStateOf(false)}
    var imageData1: ImageData? by remember { mutableStateOf(null)}
    var imageData2: ImageData? by remember { mutableStateOf(null)}
    var imageDataNext: ImageData? by remember { mutableStateOf(null)}
    var imageDataPrev: ImageData? by remember { mutableStateOf(null)}
    var secondVisible by remember { mutableStateOf(false)}
    val alpha = remember { androidx.compose.animation.core.Animatable(1F) }

    LaunchedEffect(true) {
        scope.launch {
            loadAsync(0)?.let {
                imageData1 = loadImageData(it)
            }
            loadAsync(1)?.let {
                imageDataNext = loadImageData(it)
            }
        }

    }

    LaunchedEffect(secondVisible) {
        alpha.animateTo(
            when (secondVisible) {
                false -> 1F
                true -> 0F
            },
            animationSpec = tween(1000)
        )
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
            scope.launch {
                secondVisible = !secondVisible
                if (index++ < count - 2)
                    loadAsync(index + 1)?.let {
                        imageDataNext = loadImageData(it)
                        loading = false
                    } else
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
            scope.launch {
                secondVisible = !secondVisible
                if (index-- > 1)
                    loadAsync(index - 1)?.let {
                        imageDataPrev = loadImageData(it)
                        loading = false
                    } else
                    loading = false
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .draggable(
            orientation = Orientation.Horizontal,
            state = rememberDraggableState { delta ->
                if (delta > -20 && delta < 0)
                    next()
                else if (delta < 20 && delta > 0)
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
        Image(
            modifier = Modifier
                .then(
                    if (imageData1?.angle != 0f) {
                        Modifier
                            .rotate(imageData1?.angle ?: 0f)
                            .scale(
                                (imageData1?.bitmap?.height?.toFloat()
                                    ?: 1f) / (imageData1?.bitmap?.width?.toFloat() ?: 1f)
                            )
                    } else
                        Modifier
                )
                .align(Alignment.Center)
                .alpha(alpha.value),
            bitmap = imageData1?.bitmap?.asImageBitmap()
                ?: BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.emptypics
                ).asImageBitmap(),
            contentDescription = "Image",
        )
        Image(
            modifier = Modifier
                .then(
                    if (imageData2?.angle != 0f) {
                        Modifier
                            .rotate(imageData2?.angle ?: 0f)
                            .scale(
                                (imageData2?.bitmap?.height?.toFloat()
                                    ?: 1f) / (imageData2?.bitmap?.width?.toFloat() ?: 1f)
                            )
                    } else
                        Modifier
                )
                .align(Alignment.Center)
                .alpha(1f - alpha.value),
            bitmap = imageData2?.bitmap?.asImageBitmap()
                ?: BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.emptypics
                ).asImageBitmap(),
            contentDescription = "Image",
        )
    }
}

data class ImageData(
    val bitmap: Bitmap,
    val angle: Float
)

fun loadImageData(bitmapBytes: ByteArray): ImageData {
    val angle = bitmapBytes.inputStream().use {
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
    return ImageData(BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size), angle)
}

