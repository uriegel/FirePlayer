package de.uriegel.fireplayer.ui

import android.content.Context
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
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.controller.ImageData
import de.uriegel.fireplayer.controller.ImagePagerController
import de.uriegel.fireplayer.extensions.onKeyDown
import kotlinx.coroutines.flow.MutableSharedFlow

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
    count: Int,
    loadAsync: suspend (Int)-> MediaContent
) {
    val context = LocalContext.current
    val nextFlow = remember { MutableSharedFlow<Boolean>(extraBufferCapacity = 1) }
    val imageDataFlow = remember { MutableSharedFlow<ImageData>(extraBufferCapacity = 1) }
    var loading by remember { mutableStateOf(false)}
//    var imageDataPrev: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}

//    fun next() {
//        if (!loading && currentPosition < count - 1) {
//            if (currentSecondVisible) {
//                Log.i("FOTO", "2nd visible")
//                imageData1 = imageDataNext
//                imageDataPrev = imageData2
//            } else {
//                Log.i("FOTO", "2nd not visible")
//                imageData2 = imageDataNext
//                imageDataPrev = imageData1
//            }
//            loading = true
//            secondVisible = !currentSecondVisible
//            scope.launch {
//                val newIndex = currentPosition + 1
//                if (newIndex < count - 1)
//                    imageDataNext = loadImageData(loadAsync(newIndex + 1))
//                loading = false
//                Log.i("FOTO", "next launched: $newIndex")
//                onPositionChanged(newIndex)
//            }
//        }
//    }
//
//    fun previous() {
//        if (!loading && currentPosition != 0) {
//            if (currentSecondVisible) {
//                imageData1 = imageDataPrev
//                imageDataNext = imageData2
//            } else {
//                imageData2 = imageDataPrev
//                imageDataNext = imageData1
//            }
//            loading = true
//            secondVisible = !currentSecondVisible
//            scope.launch {
//                val newIndex = currentPosition - 1
//                if (newIndex > 1)
//                    imageDataPrev = loadImageData(loadAsync(newIndex - 1))
//                loading = false
//                onPositionChanged(newIndex)
//            }
//        }
//    }
    ImagePagerController(nextFlow, imageDataFlow, loadAsync)
    Box(modifier = Modifier
        .fillMaxSize()
        .draggable(
            orientation = Orientation.Horizontal,
            state = rememberDraggableState {
                if (it > -20 && it < 0)
                    nextFlow.tryEmit(true)
                else if (it < 20 && it > 0)
                    nextFlow.tryEmit(false)
            }
        )
        .onKeyDown(context) { _, evt ->
            when (evt?.keyCode) {
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    nextFlow.tryEmit(true)
                    true
                }

                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    nextFlow.tryEmit(false)
                    true
                }

                else -> false
            }
        }
    ) {
        var imageData1: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}
        var imageData2: ImageData by remember { mutableStateOf(ImageData(null, 0f, null))}
        var secondVisible by remember { mutableStateOf(true)}

        LaunchedEffect(Unit) {
            imageDataFlow.collect {
                if (secondVisible) {
                    imageData1 = it
                    secondVisible = false
                } else {
                    imageData2 = it
                    secondVisible = true
                }
            }
        }

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

