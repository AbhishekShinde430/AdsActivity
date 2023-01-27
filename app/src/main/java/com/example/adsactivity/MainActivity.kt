package com.example.adsactivity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.adsactivity.ui.theme.AdsActivityTheme
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

class MainActivity : ComponentActivity() {
    val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}

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
                        LoadBannerAd(
                            adId = "ca-app-pub-3940256099942544/6300978111",
                            AdSize.MEDIUM_RECTANGLE
                        )

                        //banner ads
                        LoadBannerAd(adId = "ca-app-pub-3940256099942544/6300978111", AdSize.BANNER)

                        //banner ads
                        LoadBannerAd(
                            adId = "ca-app-pub-3940256099942544/6300978111",
                            AdSize.LARGE_BANNER
                        )

                        //Adaptive Ads
                        AdaptiveAdsBanner(this@MainActivity,"ca-app-pub-3940256099942544/6300978111")


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




