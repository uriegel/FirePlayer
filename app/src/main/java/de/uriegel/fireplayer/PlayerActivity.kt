package de.uriegel.fireplayer

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import de.uriegel.activityextensions.async.delay
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder

class PlayerActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        fun setFullscreen() {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
        setFullscreen()

        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            launch {
                // Note that system bars will only be "visible" if none of the
                // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    // TODO: The system bars are visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or
                    // other navigational controls.
                    delay(3000)
                    setFullscreen()
                }
            }
        }

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
            player!!.addVideoListener(object : VideoListener {
                override fun onVideoSizeChanged(
                    width: Int,
                    height: Int,
                    unappliedRotationDegrees: Int,
                    pixelWidthHeightRatio: Float
                ) {
                    playerContainer.setAspectRatio(pixelWidthHeightRatio * width / height)
                }

                override fun onRenderedFirstFrame() {}
            })

            playerView.player = player
            player!!.playWhenReady = true
            //player.seekTo()
        }
        val dataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(
                this,
                getString(R.string.app_name)
            )
        )
        val uriString = "${MainActivity.url}/video/${URLEncoder.encode(film, "utf-8")}".replace("+", "%20")

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
