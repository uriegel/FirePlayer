package de.uriegel.fireplayer

import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.activity_player.*
import java.net.URLEncoder


class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val intent = intent
        film = intent.getStringExtra("film")!!
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
            player!!.addVideoListener(object: VideoListener{
                override fun onVideoSizeChanged(
                    width: Int,
                    height: Int,
                    unappliedRotationDegrees: Int,
                    pixelWidthHeightRatio: Float
                ) { playerContainer.setAspectRatio(pixelWidthHeightRatio * width / height) }
                override fun onRenderedFirstFrame() {}
            })

            playerView.player = player
            player!!.playWhenReady = true
            //player.seekTo()
        }
        val dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(this, getString(R.string.app_name)))
        val uriString = "https://uriegel.de/video/${URLEncoder.encode(film, "utf-8")}"
        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(uriString))

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

    var player: SimpleExoPlayer? = null
    lateinit var film: String
}
