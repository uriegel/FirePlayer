package de.uriegel.fireplayer

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*
import java.lang.Exception


class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        playerView?.showController()
        return super.onKeyDown(keyCode, event)
    }

    private fun initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(this), DefaultTrackSelector(), DefaultLoadControl()
            )
            playerView.player = player
            player!!.playWhenReady = false
            //player.seekTo()
        }
        val dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(this, getString(R.string.app_name)))
        val uriString = "https://uriegel.de/video/film.mp4"
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(uriString))
        playerContainer.setAspectRatio(16f/9f)
        player!!.prepare(mediaSource)
    }

    private fun releasePlayer() {
        if (player != null) {
//            playbackPosition = player.getCurrentPosition()
//            currentWindow = player.getCurrentWindowIndex()
            player!!.release()
            player = null
        }
    }

    var player: ExoPlayer? = null
}
