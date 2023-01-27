package com.example.adsactivity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.example.adsactivity.databinding.ActivityMainBinding
import com.example.adsactivity.ui.theme.AdsActivityTheme
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAdOptions

class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityMainBinding

    val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        Ads.initialize(this)
        Ads.loadSticyAds(this,binding.adViewBottomContainer)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AdsActivityTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.surface
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {

                            //naive ads
                            NativeAds(adId = "ca-app-pub-3940256099942544/2247696110")

                            //banner ads
                            LoadBannerAd(adId = "ca-app-pub-3940256099942544/6300978111", AdSize.MEDIUM_RECTANGLE)

                            //banner ads
                            LoadBannerAd(adId = "ca-app-pub-3940256099942544/6300978111", AdSize.BANNER)

                            //banner ads
                            LoadBannerAd(adId = "ca-app-pub-3940256099942544/6300978111", AdSize.LARGE_BANNER)

                            //Adaptive Ads
                            AdaptiveAdsBanner(this@MainActivity,"ca-app-pub-3940256099942544/6300978111")

                        }
                    }
                }
            }
        }

    }




    @Composable
    fun NativeAds(adId: String) {
        val nativeAdState by mainViewModel.nativeAdState.collectAsState()
        val rememberCustomNativeAdState = rememberCustomNativeAdState(
            adUnit = adId,
            nativeAdOptions = NativeAdOptions.Builder()
                .setVideoOptions(
                    VideoOptions.Builder()
                        .setStartMuted(true).setClickToExpandRequested(true)
                        .build()
                ).setRequestMultipleImages(true)
                .build(),
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    mainViewModel.updateNativeAdState(AdState(isSuccess = true))
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.d("TAG", "onAdFailedToLoad: $p0")
                    mainViewModel.updateNativeAdState(
                        nativeAdState = AdState(
                            isError = true,
                            errorMessage = p0.message
                        )
                    )
                }
            },
            activity = this@MainActivity
        )
        NativeAdsSection(
            nativeAdState = nativeAdState,
            rememberNativeAdState = rememberCustomNativeAdState
        )
    }


}




