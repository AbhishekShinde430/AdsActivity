package com.example.adsactivity.VideoListing

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.adsactivity.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun VideoPlayerWithControls(exoPlayer: ExoPlayer) {
    val context = LocalContext.current
    val playerView = remember {
        val layout = LayoutInflater.from(context).inflate(R.layout.player_view, null)
        val playerView = (layout.findViewById(R.id.playerView) as StyledPlayerView).apply {
            player = exoPlayer
            exoPlayer.volume = 0f
            setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
        }
        val muteUnmuteImageView= layout.findViewById<ImageView>(R.id.exo_pause)
        muteUnmuteImageView.findViewById<ImageView>(R.id.exo_pause).setOnClickListener {
            /*if (exoPlayer.isPlaying){
                exoPlayer.pause()
            }else{
                exoPlayer.play()
            }*/



            if (exoPlayer.volume==0f) {
                muteUnmuteImageView.setBackgroundResource(R.drawable.ic_unmute)
                exoPlayer.setVolume(1f)
            } else {
                muteUnmuteImageView.setBackgroundResource(R.drawable.ic_mute)
                exoPlayer.setVolume(0f)
            }

        }
        layout.setOnClickListener {
            Log.d("TAG", "VideoPlayerWithControls: clickedexo")
        }
        layout.id = View.generateViewId()
        playerView
    }

    AndroidView(
        { playerView },
        Modifier
            .height(256.dp)
            .background(Color.Black)
    )
}