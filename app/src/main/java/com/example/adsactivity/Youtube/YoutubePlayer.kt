package com.example.adsactivity.Youtube

import android.app.Activity
import android.content.res.Configuration
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberImagePainter
import com.example.adsactivity.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import androidx.annotation.NonNull
import androidx.compose.ui.platform.LocalConfiguration


//AIzaSyDBMa0q64qTG2oeqXxYcrrYacxAp9AcUjU

/*@Composable
fun YoutubeScreen(
    videoId: String,
    isPlaying: Boolean,
    childFragmentManager: FragmentManager
) {
    val url = remember {
        "https://img.youtube.com/vi/$videoId/0.jpg"
    }


    if (isPlaying) {
        AndroidView(
            {
                val playerView = LayoutInflater.from(it).inflate(R.layout.yt_player, null, false)
                val fragment = YouTubePlayerSupportFragmentX().apply {
                    initialize("AIzaSyDBMa0q64qTG2oeqXxYcrrYacxAp9AcUjU",
                        object : YouTubePlayer.OnInitializedListener {
                            override fun onInitializationSuccess(
                                p0: YouTubePlayer.Provider?,
                                p1: YouTubePlayer?,
                                p2: Boolean
                            ) {
                                p1?.loadVideo(videoId)
                                p1?.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
                            }

                            override fun onInitializationFailure(
                                provider: YouTubePlayer.Provider,
                                result: YouTubeInitializationResult
                            ) {

                            }
                        })

                }
                childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.youtube_frag_view, fragment)
                }
//                val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
//                transaction.replace(R.id.youtube_frag_view, fragment)
//                transaction.commit()
                playerView
            },
            Modifier
                .height(256.dp)
                .background(Color.Black)
        )
    } else {
        *//*Image(
            painter = painterResource(id = R.drawable.black),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .size(256.dp),
            contentScale = ContentScale.Crop
        )*//*


        Image(
            painter = rememberImagePainter(data = url, builder = {
                crossfade(true)
                size(512, 512)
                placeholder(R.drawable.black)
            }),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .size(256.dp),
            contentScale = ContentScale.Crop
        )
    }


}*/

@Composable
fun YoutubeScreenLib(
    videoId: String,
    isPlaying: Boolean,
    activity: FragmentActivity,
) {
    Box(
        modifier = Modifier
            .height(256.dp)
            .fillMaxWidth()
            .background(Color.Black, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (isPlaying) {
           // YoutubePlayerLibraryView(videoId)
            CustomYoutubePlayerLibraryView(videoId = videoId, activity = activity)
        } else {
            ThumNailVideoView("https://img.youtube.com/vi/$videoId/0.jpg")
        }
    }




}

@Composable
fun ThumNailVideoView(s: String) {
    Image(
        painter = rememberImagePainter(data = s, builder = {
            crossfade(true)
            size(512, 512)
            placeholder(R.drawable.black)
        }),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .size(256.dp),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun YoutubePlayerLibraryView(videoId: String) {
    val context = LocalContext.current
    val playerView = remember {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_ytlib_player, null, false)
        val playerView = layout.findViewById(R.id.youtube_player_view) as YouTubePlayerView

        playerView.addYouTubePlayerListener(
            object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)

                    youTubePlayer.loadVideo(videoId = videoId, 0f)

                }

            }
        )
        playerView
    }
    AndroidView(factory = {
        playerView
    }, update = { view ->
    })
}

@Composable
fun CustomYoutubePlayerLibraryView(videoId: String,activity: Activity) {
    val context = LocalContext.current
    val lifecycleEvent = rememberLifecycleEvent()
    var myouTubePlayer: YouTubePlayer?= null




    val playerView = remember {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_ytlib_player, null, false)
        val youTubePlayerView = layout.findViewById(R.id.youtube_player_view) as YouTubePlayerView
        val customPlayerUi=youTubePlayerView.inflateCustomPlayerUi(R.layout.layout_custom_youtube)


        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val customPlayerUiController = CustomPlayerUiController(
                    activity,
                    customPlayerUi,
                    youTubePlayer,
                    youTubePlayerView,
                   "muted"
                )
                youTubePlayer.addListener(customPlayerUiController)
                youTubePlayerView.addFullScreenListener(customPlayerUiController)
                //val defaultPlayerUiController = DefaultPlayerUiController(youTubePlayerView, youTubePlayer)
                //youTubePlayerView.setCustomPlayerUi(defaultPlayerUiController.rootView);
                youTubePlayer.loadVideo(videoId = videoId,0f)
                youTubePlayer.mute()


            }


        }

        // disable web ui

        // disable web ui
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0)
            .build()
        youTubePlayerView.enableAutomaticInitialization= false
        youTubePlayerView.initialize(listener,options)
        youTubePlayerView
    }






    AndroidView(factory = {
        playerView
    }, update = { view ->
    })
}

@Composable
fun rememberLifecycleEvent(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current): Lifecycle.Event {
    var state by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    return state
}

