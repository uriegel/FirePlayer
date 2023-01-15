package de.uriegel.fireplayer.ui

import android.graphics.Bitmap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.launch

@Composable
fun ImageCrossFadePager(
    count: Int,
    loadAsync: suspend (Int)-> Bitmap?
) {
    val scope = rememberCoroutineScope()
    var index by remember { mutableStateOf(0) }
    val alpha = remember { androidx.compose.animation.core.Animatable(0F) }

    LaunchedEffect(index) {
        alpha.animateTo(
            when (index.mod(3)) {
                0 -> 0F
                1 -> 1F
                2 -> 2F
                else -> 0F
            },
            animationSpec = tween(2000))
    }

    Box(modifier =  Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(if (alpha.value <= 1) 1f - alpha.value else 0F),
            contentDescription = "Image",
            loadAsync = { loadAsync(0) }
        )
        AsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(if (alpha.value <=1) alpha.value else 2F - alpha.value),
            contentDescription = "Image",
            loadAsync = { loadAsync(1) }
        )
        AsyncImage(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(if (alpha.value >=1) alpha.value - 1F else 0F),
            contentDescription = "Image",
            loadAsync = { loadAsync(2) }
        )
        Button({
            scope.launch {
                index++
            }
        }) {
            Text("Weiter")
        }
    }
}
