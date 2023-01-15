package de.uriegel.fireplayer.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import de.uriegel.fireplayer.R
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

    Box(modifier =  Modifier.fillMaxSize()) {
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
        Row {
            Button({
                if (index != 0) {
                    if (secondVisible) {
                        bitmap1 = bitmapPrev
                        bitmapNext = bitmap2
                    } else {
                        bitmap2 = bitmapPrev
                        bitmapNext = bitmap1
                    }
                    scope.launch {
                        secondVisible = !secondVisible
                        if (index > 1)
                            loadAsync(--index - 1)?.let {
                                bitmapPrev = it
                            }
                    }
                }
            }) {
                Text("Zurück")
            }
            Button({
                if (index < count - 1) {
                    if (secondVisible) {
                        bitmap1 = bitmapNext
                        bitmapPrev = bitmap2
                    } else {
                        bitmap2 = bitmapNext
                        bitmapPrev = bitmap1
                    }
                    scope.launch {
                        secondVisible = !secondVisible
                        if (index < count - 2)
                            loadAsync(++index + 1)?.let {
                                bitmapNext = it
                            }
                    }
                }
            }) {
                Text("Weiter")
            }
        }
    }
}



