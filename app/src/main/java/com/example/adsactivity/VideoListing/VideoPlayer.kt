package com.example.adsactivity.VideoListing

import android.view.LayoutInflater
import android.view.View
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
fun VideoPlayer(
    exoPlayer: ExoPlayer,
    onControllerVisibilityChanged: (uiVisible: Boolean) -> Unit
) {
    val context = LocalContext.current
    val playerView = remember {
        val layout = LayoutInflater.from(context).inflate(R.layout.player_view, null, false)
        val playerView = layout.findViewById(R.id.playerView) as StyledPlayerView
        playerView.apply {
            setControllerVisibilityListener(StyledPlayerView.ControllerVisibilityListener {
                onControllerVisibilityChanged(it == View.VISIBLE)
            })
            player = exoPlayer
        }
    }

    AndroidView(
        { playerView },
        Modifier
            .height(256.dp)
            .background(Color.Black)
    )
}