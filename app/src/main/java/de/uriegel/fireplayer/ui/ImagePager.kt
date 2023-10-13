package de.uriegel.fireplayer.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.KeyEvent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import de.uriegel.fireplayer.R
import de.uriegel.fireplayer.extensions.onKeyDown
import kotlinx.coroutines.launch

@Composable
fun ImageCrossFadePager(
    count: Int,
    loadAsync: suspend (Int)-> Bitmap?
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var secondVisible by remember { mutableStateOf(false)}
    val alpha = remember { androidx.compose.animation.core.Animatable(1F) }
    var index by remember { mutableStateOf(0)}
    var loading by remember { mutableStateOf(false)}
    var bitmap1: Bitmap? by remember { mutableStateOf(null)}
    var bitmap2: Bitmap? by remember { mutableStateOf(null)}
    var bitmapNext: Bitmap? by remember { mutableStateOf(null)}
    var bitmapPrev: Bitmap? by remember { mutableStateOf(null)}

    LaunchedEffect(true) {
        scope.launch {
            loadAsync(0)?.let {
                bitmap1 = it
            }
            loadAsync(1)?.let {
                bitmapNext = it
            }
        }

    }

    LaunchedEffect(secondVisible) {
        alpha.animateTo(
            when (secondVisible) {
                false -> 1F
                true -> 0F
            },
            animationSpec = tween(1000))
    }

    Box(modifier =  Modifier
        .fillMaxSize()
        .onKeyDown(context) { _, evt ->
            when (evt?.keyCode) {
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    if (!loading && index < count - 1) {
                        if (secondVisible) {
                            bitmap1 = bitmapNext
                            bitmapPrev = bitmap2
                        } else {
                            bitmap2 = bitmapNext
                            bitmapPrev = bitmap1
                        }
                        loading = true
                        scope.launch {
                            secondVisible = !secondVisible
                            if (index++ < count - 2)
                                loadAsync(index + 1)?.let {
                                    bitmapNext = it
                                    loading = false
                                } else
                                loading = false
                        }
                    }
                    true
                }
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    if (!loading && index != 0) {
                        if (secondVisible) {
                            bitmap1 = bitmapPrev
                            bitmapNext = bitmap2
                        } else {
                            bitmap2 = bitmapPrev
                            bitmapNext = bitmap1
                        }
                        loading = true
                        scope.launch {
                            secondVisible = !secondVisible
                            if (index-- > 1)
                                loadAsync(index - 1)?.let {
                                    bitmapPrev = it
                                    loading = false
                                } else
                                loading = false

                        }
                    }
                    true
                }
                else -> false
            }
        }
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(alpha.value),
            bitmap = bitmap1?.asImageBitmap()
                ?: BitmapFactory.decodeResource(context.resources,
                    R.drawable.emptypics).asImageBitmap(),
            contentDescription = "Image",
        )
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(1f - alpha.value),
            bitmap = bitmap2?.asImageBitmap()
                ?: BitmapFactory.decodeResource(context.resources,
                    R.drawable.emptypics).asImageBitmap(),
            contentDescription = "Image",
        )
    }
}