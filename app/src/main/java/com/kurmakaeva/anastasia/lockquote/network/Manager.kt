package com.kurmakaeva.anastasia.lockquote.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Manager(context: Context) {
    private val factory: Factory = Factory()
    private val connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val callback: Callback = Callback(connectivityManager)

    val result : LiveData<NetworkResult>
        get() = callback.result

    fun registerCallback() {
        connectivityManager.registerNetworkCallback(
            factory.wifiRequest(),
            callback
        )
    }

    fun unregisterCallback() {
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

class ConnectivityLiveData(private val connectivityManager: ConnectivityManager): MutableLiveData<NetworkResult>() {
    override fun onActive() {
        val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
            this.postValue(NetworkResult.CONNECTED)
        } else {
            this.postValue(NetworkResult.DISCONNECTED)
        }
    }

    override fun onInactive() {
    }
}