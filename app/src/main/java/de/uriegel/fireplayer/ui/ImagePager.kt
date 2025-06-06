package de.uriegel.fireplayer.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val tween = 2000

//TODO ImagePager Control
//TODO ImagePager Control on Next sets the next image (already loaded)
//TODO ImagePager Control on Next sets the next image and asynchronously loads the next next image
//TODO ImagePager Control States: current image, next image, previous image
//TODO ImagePager Control only change states when loaded

//TODO ImagePager Control starting with state machine and logging (when image loaded):
// on next, p:1, c: 2 n: 3
// on next, p:2, c: 3 n: 4
// on next, p:3, c: 4 n: 5
// on prev, p:2, c: 3 n: 4

//TODO ImagePager with  O N E  variable imageData
//TODO ImagePager changes between old image and new image

@Composable
fun ImagePager(
    position: Int,
    onPositionChanged: (Int)->Unit,
    count: Int,
    loadAsync: suspend (Int)-> MediaContent
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var secondVisible by remember { mutableStateOf(false)}
    var loading by remember { mutableStateOf(false)}
    var imageData1: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}
    var imageData2: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}
    var imageDataNext: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}
    var imageDataPrev: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}
    val currentPosition by rememberUpdatedState(position)
    val currentSecondVisible by rememberUpdatedState(secondVisible)
    Log.i("FOTO", "render: $position")
    LaunchedEffect(true) {
        Log.i("FOTO", "launch: ")
        scope.launch {
            imageData1 = loadImageData(loadAsync(0))
            imageDataNext = loadImageData(loadAsync(1))
        }
    }

    fun next() {
        if (!loading && currentPosition < count - 1) {
            if (currentSecondVisible) {
                Log.i("FOTO", "2nd visible")
                imageData1 = imageDataNext
                imageDataPrev = imageData2
            } else {
                Log.i("FOTO", "2nd not visible")
                imageData2 = imageDataNext
                imageDataPrev = imageData1
            }
            loading = true
            secondVisible = !currentSecondVisible
            scope.launch {
                val newIndex = currentPosition + 1
                if (newIndex < count - 1)
                    imageDataNext = loadImageData(loadAsync(newIndex + 1))
                loading = false
                Log.i("FOTO", "next launched: $newIndex")
                onPositionChanged(newIndex)
            }
        }
    }

    fun previous() {
        if (!loading && currentPosition != 0) {
            if (currentSecondVisible) {
                imageData1 = imageDataPrev
                imageDataNext = imageData2
            } else {
                imageData2 = imageDataPrev
                imageDataNext = imageData1
            }
            loading = true
            secondVisible = !currentSecondVisible
            scope.launch {
                val newIndex = currentPosition - 1
                if (newIndex > 1)
                    imageDataPrev = loadImageData(loadAsync(newIndex - 1))
                loading = false
                onPositionChanged(newIndex)
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
                    Log.i("FOTO", "KEYCODE_DPAD_RIGHT: $position")
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
        Log.i("FOTO", "Render Animation Box: 2nd visible: $secondVisible")
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
            Log.i("FOTO", "Animation 2nd not visible")
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
            Log.i("FOTO", "Animation 2nd visible")
            MediaContent(imageData2, context)
        }
    }
}

@Composable
private fun MediaContent(imageData: ImageData, context: Context) {
    Box {
        if (imageData.bitmap != null)
            RotatableImage(imageData, context)
        else if (imageData.videoUrl != null)
            VideoClip(imageData.videoUrl)
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

