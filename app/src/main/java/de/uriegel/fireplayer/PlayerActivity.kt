package de.uriegel.fireplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*

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
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse("https://uriegel.de/video/f3.mp4"))
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
