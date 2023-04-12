package com.example.adsactivity.VideoListing

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class VideoViewModel(application: Application):AndroidViewModel(application) {

    val videos = MutableStateFlow<List<VideoItem>>(listOf())
    val currentlyPlayingIndex = MutableStateFlow<Int?>(null)

    init {
        populateListWithFakeData()
    }

    private fun populateListWithFakeData() {
        val testVideos = listOf(
            VideoItem(
                1,
                "",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
                "idLFj44bkjk"

            ),
            VideoItem(
                2,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
            ),
            VideoItem(
                3,
                "",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
                "43e8dPUt7RY"

            ),
            VideoItem(
                4,
                "",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
                "nNqOeOTikB8"

            ),
            VideoItem(
                5,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
            ),
            VideoItem(
                6,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
            ),
            VideoItem(
                7,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerFun.jpg"
            ),
            VideoItem(
                8,
                "https://joy1.videvo.net/videvo_files/video/free/video0469/large_watermarked/_import_6174eb86a3bb59.68710093_preview.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerJoyrides.jpg"
            ),
            VideoItem(
                9,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerMeltdowns.jpg"
            ),
            VideoItem(
                10,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/Sintel.jpg"
            ),
            VideoItem(
                11,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/SubaruOutbackOnStreetAndDirt.jpg"
            ),
            VideoItem(
                12,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/TearsOfSteel.jpg"
            ),
            VideoItem(
                13,
                "https://joy1.videvo.net/videvo_files/video/free/video0469/large_watermarked/_import_6174efef1bff75.45760209_preview.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
            ),
            VideoItem(
                14,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
            ),
            VideoItem(
                15,
                "",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerFun.jpg",
                "CTFtOOh47oo"
            ),
        )
        videos.value = testVideos
    }


    fun onPlayVideoClick(videoIndex: Int) {
        when (currentlyPlayingIndex.value) {
            null -> currentlyPlayingIndex.value = videoIndex
            videoIndex -> {
                currentlyPlayingIndex.value = null
            }
            else -> {
                currentlyPlayingIndex.value = videoIndex
            }
        }
    }


}