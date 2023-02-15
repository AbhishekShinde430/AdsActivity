package com.example.adsactivity.VideoListing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.ContentAlpha.medium
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.adsactivity.R
import androidx.compose.material.Icon
import androidx.compose.material.Shapes
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.adsactivity.databinding.ActivityVideoListingBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.lang.Math.abs

class VideoListingActivity : AppCompatActivity() {

    lateinit var binding:ActivityVideoListingBinding
    val viewModel by viewModels<VideoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FeedsScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FeedsScreen(viewModel: VideoViewModel) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val exoPlayer = remember(context) { ExoPlayer.Builder(context).build() }
    val listState = rememberLazyListState()

    val videos by viewModel.videos.collectAsStateWithLifecycle()
    val playingItemIndex by viewModel.currentlyPlayingIndex.collectAsStateWithLifecycle()

    var isCurrentItemVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        snapshotFlow {
            listState.visibleAreaContainsItem(playingItemIndex, videos)
        }.collect { isItemVisible ->
            isCurrentItemVisible = isItemVisible
        }
    }

    LaunchedEffect(playingItemIndex) {
        if (playingItemIndex == null) {
            exoPlayer.pause()
        } else {
            val video = videos[playingItemIndex!!]
            exoPlayer.setMediaItem(MediaItem.fromUri(video.mediaUrl), video.lastPlayedPosition)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    LaunchedEffect(isCurrentItemVisible) {
        if (!isCurrentItemVisible && playingItemIndex != null) {
            viewModel.onPlayVideoClick(exoPlayer.currentPosition, playingItemIndex!!)
        }
    }

    DisposableEffect(exoPlayer) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (playingItemIndex == null) return@LifecycleEventObserver
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
        state = listState,
        modifier = Modifier.fillMaxSize(),
    ) {
        itemsIndexed(videos, { _, video -> video.id }) { index, video ->
            Spacer(modifier = Modifier.height(16.dp))
            VideoCard(
                videoItem = video,
                exoPlayer = exoPlayer,
                isPlaying = index == playingItemIndex
            ) {
                viewModel.onPlayVideoClick(exoPlayer.currentPosition, index)
            }
        }
    }
}

@Composable
fun VideoCard(
    modifier: Modifier = Modifier,
    videoItem: VideoItem,
    isPlaying: Boolean,
    exoPlayer: ExoPlayer,
    onClick: ()->Unit
) {
    var isPlayerUiVisible by remember { mutableStateOf(false) }
    val isPlayButtonVisible = if (isPlayerUiVisible) true else !isPlaying

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black,RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (isPlaying) {
            VideoPlayer(exoPlayer) { uiVisible ->
                isPlayerUiVisible = uiVisible
            }
        }else {
            VideoThumbnail(videoItem.thumbnail)
        }
        if (isPlayButtonVisible) {
            Icon(
                painter = painterResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(72.dp)
                    .clip(remember { RoundedCornerShape(percent = 50) })
                    .clickable(onClick = onClick)
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun VideoThumbnail(url: String) {
    Image(
        painter = rememberImagePainter(data = url, builder = {
            crossfade(true)
            size(512, 512)
        }),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .size(256.dp),
        contentScale = ContentScale.Crop
    )
}

private fun LazyListState.visibleAreaContainsItem(
    currentlyPlayedIndex: Int?,
    videos: List<VideoItem>
): Boolean {
    return when {
        currentlyPlayedIndex == null -> false
        videos.isEmpty() -> false
        else -> {
            layoutInfo.visibleItemsInfo.map { videos[it.index] }
                .contains(videos[currentlyPlayedIndex])
        }
    }
}




