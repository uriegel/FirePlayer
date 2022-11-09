package de.uriegel.fireplayer

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.util.Util
import de.uriegel.fireplayer.databinding.ActivityPlayerBinding
import de.uriegel.fireplayer.room.FilmInfo
import de.uriegel.fireplayer.room.FilmInfosRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.net.URLEncoder
import java.util.*

@ExperimentalSerializationApi
class PlayerActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        val intent = intent
        film = intent.getStringExtra("film")!!

        launch {
            FilmInfosRepository.getFilmInfoAsync(film).await().elementAtOrNull(0)?.let{
                playbackPosition = it.position
                player?.seekTo(playbackPosition)
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            savedInstanceState?.let {
                playbackPosition = it.getLong(STATE_PLAYBACK_POSITION, playbackPosition)
                currentWindow = it.getInt(STATE_CURRENT_WINDOW, currentWindow)
                playWhenReady = it.getBoolean(STATE_PLAY_WHEN_READY, playWhenReady)
            }

            viewBinding.playerView.setControllerVisibilityListener {
                if (it == View.VISIBLE)
                    showSystemUI()
                else
                    hideSystemUI()
            }

            hideSystemUI()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23)
            initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23)
            initializePlayer()
        lifetimeTimer = LifetimeTimer()
        lifetimeTimer?.start()
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23)
            releasePlayer()
        lifetimeTimer?.cancel()
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23)
            releasePlayer()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        viewBinding.playerView.showController()
        if (viewBinding.playerView.isControllerVisible)
            showSystemUI()
        else
            hideSystemUI()
        return super.onKeyDown(keyCode, event)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(STATE_PLAYBACK_POSITION, playbackPosition)
        outState.putInt(STATE_CURRENT_WINDOW, currentWindow)
        outState.putBoolean(STATE_PLAY_WHEN_READY, playWhenReady)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        playbackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION, playbackPosition)
        currentWindow = savedInstanceState.getInt(STATE_CURRENT_WINDOW, currentWindow)
        playWhenReady = savedInstanceState.getBoolean(STATE_PLAY_WHEN_READY, playWhenReady)
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .build()
            .also {
                viewBinding.playerView.player = it
                val uri = "${MainActivity.url}/video/${URLEncoder.encode(film, "utf-8")}"
                    .replace("+", "%20")
                val mediaItem = MediaItem.fromUri(uri)
                it.setMediaItem(mediaItem)
                it.playWhenReady = playWhenReady
                it.seekTo(currentWindow, playbackPosition)
                it.prepare()
                it.play()
            }
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady

            launch {
                FilmInfosRepository.insertFilmInfoAsync(FilmInfo(
                    playbackPosition, Calendar.getInstance().time, film)
                ).await()
            }

            release()
        }
        player = null
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, viewBinding.root).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, viewBinding.root).show(WindowInsetsCompat.Type.systemBars())
    }

    private val viewBinding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    companion object {
        const val STATE_PLAYBACK_POSITION = "STATE_PLAYBACK_POSITION"
        const val STATE_CURRENT_WINDOW = "STATE_CURRENT_WINDOW"
        const val STATE_PLAY_WHEN_READY = "STATE_PLAY_WHEN_READY"
    }

    private var lifetimeTimer: LifetimeTimer? = null
    private var player: SimpleExoPlayer? = null
    private lateinit var film: String
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
}
