package de.uriegel.fireplayer.ui

import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.preference.PreferenceManager
import de.uriegel.fireplayer.extensions.*
import de.uriegel.fireplayer.requests.getBaseUrl
import de.uriegel.fireplayer.requests.post
import de.uriegel.fireplayer.viewmodel.DirectoryItemsViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Preview(showSystemUi = true)
@Composable
fun MusicScreen(
    @PreviewParameter(ViewModelPreviewProvider::class) viewModel: DirectoryItemsViewModel,
    path64: String? = null) {
    val path = path64?.fromBase64() ?: ""
    val filePath = path.getFilePath()

    val context = LocalContext.current
    val preferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    val sonyUrl = preferences.getString("sony_url", null)
    val sonyPsk = preferences.getString("sony_psk", null)
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            MusicPlayer(viewModel
                .items
                .filter { it.name.isMusic() }
                .map { filePath + it.name })
        }
        if ((sonyUrl?.length ?: 1) >= 6)
            Button(
                onClick={
                    scope.launch {
                        val data = SonyData("setPowerSavingMode", "1.0",
                            111, listOf(SonyDataParam("pictureOff")))
                        val content = Json.encodeToString(data)
                        post("$sonyUrl/system", content, sonyPsk)
                            .onFailure {
                                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(20.dp)
        ) {
            Text("")
        }
    }
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
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
                        it.addMediaItem(
                            MediaItem.fromUri(
                            getBaseUrl() + item.replace("+", "%20")))
                    }
                    it.prepare()
                }

            PlayerView(context).apply {
                player = exoPlayer.value
                this.setOnKeyListener { _, _, _ ->
                    showController()
                    false
                }

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

@Serializable
data class SonyDataParam(val mode: String)

@Serializable
data class SonyData(val method: String, val version: String, val id: Int, val params: List<SonyDataParam>)

class ViewModelPreviewProvider : PreviewParameterProvider<DirectoryItemsViewModel> {
    override val values = sequenceOf(
        DirectoryItemsViewModel()
    )
}

