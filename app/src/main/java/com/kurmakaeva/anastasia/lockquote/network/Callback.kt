package com.kurmakaeva.anastasia.lockquote.network

import android.net.ConnectivityManager
import android.net.Network

class Callback(val connectivityManager: ConnectivityManager): ConnectivityManager.NetworkCallback() {

    val result = ConnectivityLiveData(connectivityManager)

    override fun onLost(network: Network?) {
        result.postValue(NetworkResult.DISCONNECTED)
    }

    override fun onUnavailable() {
        result.postValue(NetworkResult.DISCONNECTED)
    }

    override fun onAvailable(network: Network?) {
        result.postValue(NetworkResult.CONNECTED)
    }
}