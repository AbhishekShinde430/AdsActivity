package com.example.adsactivity.Youtube

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.adsactivity.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class CustomPlayerUiController(
    context: Activity,
    customPlayerUi: View,
    youTubePlayer: YouTubePlayer,
    youTubePlayerView: YouTubePlayerView,
    mute: String
) : AbstractYouTubePlayerListener(), YouTubePlayerFullScreenListener {

    private var playerUi: View? = null

    private var context: Context? = null
    private var youTubePlayer: YouTubePlayer? = null
    private var youTubePlayerView: YouTubePlayerView? = null

    // panel is used to intercept clicks on the WebView, I don't want the user to be able to click the WebView directly.
    private var panel: View? = null


    private var playerTracker: YouTubePlayerTracker? = null
    private var fullscreen = false
    private var isMuted=""

    init {
        playerUi = customPlayerUi
        this.context = context
        this.youTubePlayer = youTubePlayer
        this.youTubePlayerView = youTubePlayerView
        playerTracker = YouTubePlayerTracker()
        isMuted = mute
        youTubePlayer.addListener(playerTracker!!)
        initViews(customPlayerUi)
    }

    private fun initViews(playerUi: View) {
        panel = playerUi.findViewById(R.id.panel)
        //val playPauseButton: Button = playerUi.findViewById(R.id.play_pause_button)
      //  val enterExitFullscreenButton: Button = playerUi.findViewById(R.id.enter_exit_fullscreen_button)
        /*playPauseButton.setOnClickListener { view ->
         //   if (playerTracker!!.state == PlayerState.PLAYING) youTubePlayer?.pause() else youTubePlayer?.play()
        }*/

        val muteUnMute: ImageView = playerUi.findViewById(R.id.awayImgIc)
        muteUnMute.setOnClickListener {
            if (isMuted.equals("muted")){
                isMuted="unmuted"
                muteUnMute.setBackgroundResource(R.drawable.ic_unmute)
                youTubePlayer?.unMute()
            }else{
                isMuted="muted"
                muteUnMute.setBackgroundResource(R.drawable.ic_mute)
                youTubePlayer?.mute()
            }
        }

    }



    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerState) {
        if (state == PlayerState.PLAYING || state == PlayerState.PAUSED || state == PlayerState.VIDEO_CUED) panel?.setBackgroundColor(
            ContextCompat.getColor(context!!, android.R.color.transparent)
        ) else if (state == PlayerState.BUFFERING) panel?.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                android.R.color.transparent
            )
        )
    }

    override fun onYouTubePlayerEnterFullScreen() {

    }

    override fun onYouTubePlayerExitFullScreen() {

    }


}