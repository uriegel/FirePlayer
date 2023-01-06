package de.uriegel.fireplayer.ui

import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import de.uriegel.fireplayer.extensions.*
import de.uriegel.fireplayer.requests.getBaseUrl
import de.uriegel.fireplayer.ui.theme.FirePlayerTheme
import de.uriegel.fireplayer.viewmodel.MusicViewModel

@Composable
fun MusicScreen(viewModel: MusicViewModel, path64: String?) {
    val path = path64?.fromBase64() ?: ""
    val filePath = path.getFilePath()
    MusicPlayer(viewModel
        .items
        .filter { it.name.isMusic() }
        .map { filePath + it.name })
}

@Composable
fun MusicPlayer(playList: List<String>) {
    val context = LocalContext.current

    val exoPlayer: MutableState<ExoPlayer?> = remember  { mutableStateOf(null) }
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(
        AndroidView(factory = {
            exoPlayer.value = ExoPlayer.Builder(context)
                .build()
                .also {
                    playList.forEach { item ->
                        it.addMediaItem(MediaItem.fromUri(
                            getBaseUrl() + item.replace("+", "%20")))
                    }
                    it.prepare()
                }

            StyledPlayerView(context).apply {
                player = exoPlayer.value
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

@Preview(showSystemUi = true)
@Composable
fun MusicScreenPreview() {
    FirePlayerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            MusicScreen(viewModel(), "Musik.mp3".toBase64())
        }
    }
}

