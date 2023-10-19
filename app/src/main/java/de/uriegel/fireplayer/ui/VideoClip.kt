package de.uriegel.fireplayer.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import de.uriegel.fireplayer.requests.accessDisk
import de.uriegel.fireplayer.requests.getBaseUrl
import kotlinx.coroutines.launch

@Composable
fun VideoClip(path: String?) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        VideoClipPlayer(path)
    }
}

@SuppressLint("OpaqueUnitKey")
@Composable
fun VideoClipPlayer(path: String?) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val exoPlayer = remember {
        ExoPlayer
            .Builder(context)
            .build()
            .apply {
                val mediaItem = MediaItem
                    .Builder()
                    .setUri(getBaseUrl() + path)
                    .build()
                setMediaItem(mediaItem)
                playWhenReady = true
                addListener(object: Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        //if (error.errorCode == 2001) {
                        coroutineScope.launch {
                            accessDisk()
                            play()
                        }
                        //}
                    }
                })
                prepare()
                play()
            }
    }

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false
            }
        })
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}
