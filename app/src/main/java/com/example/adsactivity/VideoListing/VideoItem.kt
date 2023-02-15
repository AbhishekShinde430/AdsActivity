package com.example.adsactivity.VideoListing

data class VideoItem(
    val id: Int,
    val mediaUrl: String,
    val thumbnail: String,
    val lastPlayedPosition: Long = 0
)