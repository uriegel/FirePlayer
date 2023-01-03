package de.uriegel.fireplayer.ui

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import de.uriegel.fireplayer.extensions.fromBase64
import de.uriegel.fireplayer.extensions.hideSystemUi
import de.uriegel.fireplayer.extensions.showSystemUi
import de.uriegel.fireplayer.requests.getBaseUrl

@Composable
fun VideoScreen(fullscreenMode: MutableState<Boolean>, path64: String?) {
    val context = LocalContext.current
    val path = path64!!.fromBase64()

    val exoPlayer = ExoPlayer.Builder(context)
        .build()
        .also {
            val mediaItem = MediaItem
                .Builder()
                .setUri(getBaseUrl() + path)
                .build()
            it.setMediaItem(mediaItem)
            it.prepare()
        }

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(
        AndroidView(factory = {
            StyledPlayerView(context).apply {
                player = exoPlayer
                setControllerVisibilityListener(StyledPlayerView.ControllerVisibilityListener {
                    if (it == View.VISIBLE)
                        context.showSystemUi()
                    else
                        context.hideSystemUi()
                })
            }
        })
    ) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    fullscreenMode.value = true
                    exoPlayer.play()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    fullscreenMode.value = false
                    exoPlayer.pause()
                }
                else -> {}
            }
        }
        val lifecycle = lifecycleOwner.value.lifecycle
        lifecycle.addObserver(observer)

        onDispose { exoPlayer.release() }
    }
}
