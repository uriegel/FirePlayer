package de.uriegel.fireplayer.ui

import android.app.Application
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import de.uriegel.fireplayer.extensions.*
import de.uriegel.fireplayer.requests.accessDisk
import de.uriegel.fireplayer.requests.getBaseUrl
import de.uriegel.fireplayer.room.FilmInfo
import de.uriegel.fireplayer.viewmodel.VideoViewModel
import de.uriegel.fireplayer.viewmodel.VideoViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@Composable
fun VideoScreen(path64: String?) {
    val context = LocalContext.current
    val playerView: MutableState<StyledPlayerView?> = remember { mutableStateOf(null) }
    val owner = LocalViewModelStoreOwner.current
    owner?.let {
        val viewModel: VideoViewModel = viewModel(it, "VideoViewModel", VideoViewModelFactory(
            LocalContext.current.applicationContext as Application
        ))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .onKeyDown(context) { _, _ ->
                    playerView.value?.showController()
                    false
                }
        ) {
            VideoPlayer(viewModel, path64, playerView)
        }
    }
}

@Composable
fun VideoPlayer(viewModel: VideoViewModel, path64: String?, playerView: MutableState<StyledPlayerView?>) {
    val context = LocalContext.current
    val path = path64!!.fromBase64()

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

            StyledPlayerView(context).apply {
                player = exoPlayer.value
                playerView.value = this
                this.setOnKeyListener { _, _, _ ->
                   showController()
                   false
                }

                coroutineScope.launch {
                    viewModel
                        .findAsync(path)
                        .await()
                        .elementAtOrNull(0)?.let {
                            player?.seekTo(it.position)
                        }
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
            exoPlayer.value?.run {
                viewModel.insert(FilmInfo(
                    this.currentPosition,
                    Calendar.getInstance().time,
                    path
                ))
            }
            exoPlayer.value?.release()
            exoPlayer.value = null
        }
    }
}
