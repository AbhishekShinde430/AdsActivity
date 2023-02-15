package com.example.adsactivity

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import com.example.adsactivity.databinding.ActivityMainBinding
import com.example.adsactivity.databinding.LayoutWebviewBinding
import com.example.adsactivity.ui.theme.AdsActivityTheme
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAdOptions
import java.util.ArrayList

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
                val bannerAdState by mainViewModel.bannerAdState.collectAsState()
                AdsActivityTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.surface
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {

                                items(10){
                                    Text(text = "This is Test Text", fontSize = 12.sp)
                                }


                                item{
                                    //banner ads
                                    LoadBannerAd(adId = "ca-app-pub-3940256099942544/6300978111", AdSize.MEDIUM_RECTANGLE)

                                }

                                items(10){
                                    Text(text = "This is Test Text", fontSize = 12.sp)
                                }

//                                item{
//                                    //banner ads
//
//                                    LoadBannerAd(
//                                        adId = "ca-app-pub-3940256099942544/6300978111",
//                                        AdSize.BANNER,
//                                    )
//
//                                }

//                                items(10){
//                                    Text(text = "This is Test Text", fontSize = 12.sp)
//                                }


//                                item{
//                                    //banner ads
//                                    LoadBannerAd(
//                                        adId = "ca-app-pub-3940256099942544/6300978111",
//                                        AdSize.LARGE_BANNER,
//                                    )
//                                }

                                items(10){
                                    Text(text = "This is Test Text", fontSize = 12.sp)
                                }

                                item {
                                    //naive ads
                                    NativeAds(adId = "ca-app-pub-3940256099942544/2247696110")
                                }

                                items(10){
                                    Text(text = "This is Test Text", fontSize = 12.sp)
                                }


                                item{
                                    //Adaptive Ads
                                    AdaptiveAdsBanner(this@MainActivity,"ca-app-pub-3940256099942544/6300978111")

                                }

                                items(10){
                                    Text(text = "This is Test Text", fontSize = 12.sp)
                                }



                            }
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




