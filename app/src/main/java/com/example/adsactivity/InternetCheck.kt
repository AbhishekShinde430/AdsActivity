package com.example.adsactivity

import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
//,ConnectivityReceiver.ConnectivityReceiverListener
class InternetCheck : AppCompatActivity(){
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internet_check)

      //  registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val connectivityManager = this.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
       // connectivityManager.requestNetwork(networkRequest, networkCallback)
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d("TAG", "Network Available")
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)


            val isValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            if (isValidated){
                Log.d("TAG", "hasCapability:")
            } else{
                Log.d("TAG", "Network has No Connection Capability:")
            }
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            Log.d("TAG", "onLosing: ")
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d("TAG", " Network LOst")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.d("TAG", "onUnavailable: ")
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            Log.d("TAG", "onLinkPropertiesChanged: ")
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)

            Log.d("TAG", "onBlockedStatusChanged: ")
        }
    }



}