package de.uriegel.fireplayer.ui

import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import de.uriegel.fireplayer.extensions.addWindowFlags
import de.uriegel.fireplayer.extensions.clearWindowFlags
import de.uriegel.fireplayer.extensions.hideSystemUi
import de.uriegel.fireplayer.extensions.showSystemUi
import de.uriegel.fireplayer.requests.accessDisk
import de.uriegel.fireplayer.requests.getBaseUrl
import kotlinx.coroutines.launch

@Composable
fun VideoClip(path: String?) {
    val playerView: MutableState<PlayerView?> = remember { mutableStateOf(null) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        VideoClipPlayer(path, playerView)
    }
}

@Composable
fun VideoClipPlayer(path: String?, playerView: MutableState<PlayerView?>) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
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
                    it.addListener(object: Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            //if (error.errorCode == 2001) {
                            coroutineScope.launch {
                                accessDisk()
                                exoPlayer.value?.play()
                            }
                            //}
                        }
                    })
                    it.prepare()
                }

            PlayerView(context).apply {
                player = exoPlayer.value
                playerView.value = this
                this.setOnKeyListener { _, _, _ ->
                    false
                }

                useController = false
                hideController()

                setControllerVisibilityListener(PlayerView.ControllerVisibilityListener {
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
                    context.addWindowFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    exoPlayer.value?.play()
                }
                Lifecycle.Event.ON_PAUSE -> {
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
