package de.uriegel.fireplayer.ui

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val tween = 2000

@Composable
fun ImagePager(
    count: Int,
    loadAsync: suspend (Int)-> ByteArray?
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var secondVisible by remember { mutableStateOf(false)}
    var index by remember { mutableIntStateOf(0) }
    var loading by remember { mutableStateOf(false)}
    var bitmap1: ImageData? by remember { mutableStateOf(null)}
    var bitmap2: ImageData? by remember { mutableStateOf(null)}
    var bitmapNext: ImageData? by remember { mutableStateOf(null)}
    var bitmapPrev: ImageData? by remember { mutableStateOf(null)}

    LaunchedEffect(true) {
        scope.launch {
            loadAsync(0)?.let {
                bitmap1 = loadImageData(it)
            }
            loadAsync(1)?.let {
                bitmapNext = loadImageData(it)
            }
        }
    }

    fun next() {
        if (!loading && index < count - 1) {
            if (secondVisible) {
                bitmap1 = bitmapNext
                bitmapPrev = bitmap2
            } else {
                bitmap2 = bitmapNext
                bitmapPrev = bitmap1
            }
            loading = true
            secondVisible = !secondVisible
            scope.launch {
                if (index++ < count - 2)
                    loadAsync(index + 1)?.let {
                        bitmapNext = loadImageData(it)
                    }
                loading = false
            }
        }
    }

    fun previous() {
        if (!loading && index != 0) {
            if (secondVisible) {
                bitmap1 = bitmapPrev
                bitmapNext = bitmap2
            } else {
                bitmap2 = bitmapPrev
                bitmapNext = bitmap1
            }
            loading = true
            secondVisible = !secondVisible
            scope.launch {
                if (index-- > 1)
                    loadAsync(index - 1)?.let {
                        bitmapPrev = loadImageData(it)
                    }
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
            Image(
                modifier = Modifier
                    .then(
                        if (bitmap1?.angle != 0f) {
                            Modifier
                                .rotate(bitmap1?.angle ?: 0f)
                                .scale(
                                    (bitmap1?.bitmap?.height?.toFloat()
                                        ?: 1f) / (bitmap1?.bitmap?.width?.toFloat() ?: 1f)
                                )
                        } else
                            Modifier
                    ),
                bitmap = bitmap1?.bitmap?.asImageBitmap()
                    ?: BitmapFactory.decodeResource(context.resources,
                        R.drawable.emptypics).asImageBitmap(),
                contentDescription = "Image",
            )
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
            Image(
                modifier = Modifier
                    .then(
                        if (bitmap2?.angle != 0f) {
                            Modifier
                                .rotate(bitmap2?.angle ?: 0f)
                                .scale(
                                    (bitmap2?.bitmap?.height?.toFloat()
                                        ?: 1f) / (bitmap2?.bitmap?.width?.toFloat() ?: 1f)
                                )
                        } else
                            Modifier
                    ),
                bitmap = bitmap2?.bitmap?.asImageBitmap()
                    ?: BitmapFactory.decodeResource(context.resources,
                        R.drawable.emptypics).asImageBitmap(),
                contentDescription = "Image",
            )
        }
    }
}

private suspend fun loadImageData(bytes: ByteArray): ImageData =
    withContext(Dispatchers.IO) {
        val angle = bytes.inputStream().use {
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
        return@withContext ImageData(BitmapFactory.decodeByteArray(bytes, 0, bytes.size), angle)
    }

private data class ImageData(
    val bitmap: Bitmap,
    val angle: Float
)
