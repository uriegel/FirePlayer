package de.uriegel.fireplayer.ui

import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import de.uriegel.fireplayer.extensions.*
import de.uriegel.fireplayer.requests.getBaseUrl

// TODO rotate keep video state
// TODO Room

@Composable
fun VideoScreen(fullscreenMode: MutableState<Boolean>, path64: String?) {
    val context = LocalContext.current
    val playerView: MutableState<StyledPlayerView?> = remember  { mutableStateOf(null) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .onKeyDown(context) { _, _ ->
                playerView.value?.showController()
                false
            }
    ) {
        VideoPlayer(fullscreenMode, path64, playerView)
    }
}

@Composable
fun VideoPlayer(fullscreenMode: MutableState<Boolean>, path64: String?, playerView: MutableState<StyledPlayerView?>) {
    val context = LocalContext.current
    val path = path64!!.fromBase64()

    val exoPlayer: MutableState<ExoPlayer?> = remember  { mutableStateOf(null) }
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(
        AndroidView(factory = {
            exoPlayer.value = ExoPlayer.Builder(context)
                .build()
                .also {
                    val mediaItem = MediaItem
                        .Builder()
                        .setUri(getBaseUrl() + path)
                        .build()
                    it.setMediaItem(mediaItem)
                    it.prepare()
                }

            StyledPlayerView(context).apply {
                player = exoPlayer.value
                playerView.value = this
                this.setOnKeyListener { _, _, _ ->
                   showController()
                   false
                }

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
                    context.addWindowFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    exoPlayer.value?.play()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    fullscreenMode.value = false
                    context.clearWindowFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    exoPlayer.value?.pause()
                }
                else -> {}
            }
        }
        val lifecycle = lifecycleOwner.value.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            exoPlayer.value?.release()
            exoPlayer.value = null
        }
    }
}
