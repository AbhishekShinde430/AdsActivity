package com.example.adsactivity.VideoListing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.adsactivity.R
import com.example.adsactivity.Youtube.YoutubeScreenLib
import com.example.adsactivity.databinding.FragmentVideoBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class VideoFragment : Fragment() {


    val viewModel by viewModels<VideoViewModel>()
    lateinit var binding: FragmentVideoBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FeedsScreen(viewModel = viewModel)
            }
        }






        return view
    }

    @Composable
    fun FeedsScreen(viewModel: VideoViewModel) {

        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val listState = rememberLazyListState()
        val exoPlayer = remember { ExoPlayer.Builder(context).build() }
        val videos by viewModel.videos.collectAsStateWithLifecycle()
        var playingVideoItem by remember { mutableStateOf(videos.firstOrNull()) }






        LaunchedEffect(Unit) {
            snapshotFlow {
                listState.playingItem(videos)
            }.collect { videoItem ->
                playingVideoItem = videoItem
            }
        }

        LaunchedEffect(playingVideoItem) {
            // is null only upon entering the screen

            if (playingVideoItem?.youTubeID.isNullOrEmpty()) {
                //exoplayer
                if (playingVideoItem == null) {
                    exoPlayer.pause()
                } else {
                    exoPlayer.setMediaItem(MediaItem.fromUri(playingVideoItem!!.mediaUrl))
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true

                }
                //Toast.makeText(this@VideoListingActivity,"Exoplayer Playing..url:${playingVideoItem?.mediaUrl}",Toast.LENGTH_SHORT).show()
            } else {
                //youtube playing
                exoPlayer.pause()

                // Toast.makeText(this@VideoListingActivity,"Youtube Playing..url:${playingVideoItem?.youTubeID}",Toast.LENGTH_SHORT).show()
            }

        }

        DisposableEffect(exoPlayer) {
            val lifecycleObserver = LifecycleEventObserver { _, event ->
                if (playingVideoItem == null) return@LifecycleEventObserver
                when (event) {
                    Lifecycle.Event.ON_START -> exoPlayer.play()
                    Lifecycle.Event.ON_STOP -> exoPlayer.pause()
                    else -> {}
                }
            }

            lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
                exoPlayer.release()
            }
        }


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = WindowInsets.systemBars
                .only(WindowInsetsSides.Vertical)
                .add(WindowInsets(left = 16.dp, right = 16.dp, bottom = 8.dp))
                .asPaddingValues()
        ) {
            items(videos) { video ->
                Spacer(modifier = Modifier.height(16.dp))
                if (video.youTubeID.isNotEmpty()) {
                    activity?.let {
                        YoutubeScreenLib(
                            videoId = video.youTubeID,
                            isPlaying = video.id == playingVideoItem?.id,
                            it
                        )
                    }
                } else {
                    AutoPlayVideoCard(
                        videoItem = video,
                        exoPlayer = exoPlayer,
                        isPlaying = video.id == playingVideoItem?.id,
                    )
                }

            }
        }
    }


}

@Composable
fun AutoPlayVideoCard(
    videoItem: VideoItem,
    isPlaying: Boolean,
    exoPlayer: ExoPlayer,
) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(Color.Black, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (isPlaying) {
            VideoPlayerWithControls(exoPlayer)
        } else {
            VideoThumbnail(videoItem.thumbnail)
        }
        Text(
            text = "${videoItem.id}",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun VideoThumbnail(url: String) {
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


private fun LazyListState.playingItem(videos: List<VideoItem>): VideoItem? {
    if (layoutInfo.visibleItemsInfo.isEmpty() || videos.isEmpty()) return null
    val layoutInfo = layoutInfo
    val visibleItems = layoutInfo.visibleItemsInfo
    val lastItem = visibleItems.last()
    val firstItemVisible = firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
    val itemSize = lastItem.size
    val itemOffset = lastItem.offset
    val totalOffset = layoutInfo.viewportEndOffset
    val lastItemVisible = lastItem.index == videos.size - 1 && totalOffset - itemOffset >= itemSize
    val midPoint = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
    val centerItems = visibleItems.sortedBy { Math.abs(it.offset + it.size / 2 - midPoint) }

    return when {
        firstItemVisible -> videos.first()
        lastItemVisible -> videos.last()
        else -> centerItems.firstNotNullOf { videos[it.index] }
    }
}
