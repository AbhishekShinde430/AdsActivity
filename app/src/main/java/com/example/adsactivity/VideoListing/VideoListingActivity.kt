package com.example.adsactivity.VideoListing

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.adsactivity.R
import com.example.adsactivity.databinding.ActivityVideoListingBinding


class VideoListingActivity : AppCompatActivity() {

    lateinit var binding: ActivityVideoListingBinding
    val viewModel by viewModels<VideoViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout_container, VideoFragment())
        transaction.commit()

    }



}









