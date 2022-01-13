package com.loskon.noteminimalism3.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Проверка интернет-соединения
 */

object InternetCheck {

    fun isConnected(context: Context): Boolean {
        val connectivityManager: ConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkConnectedAndroidM(connectivityManager)
        } else {
            checkConnected(connectivityManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkConnectedAndroidM(connectivityManager: ConnectivityManager): Boolean {
        val network: Network = connectivityManager
            .activeNetwork ?: return false

        val activeNetwork: NetworkCapabilities = connectivityManager
            .getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    @Suppress("DEPRECATION")
    private fun checkConnected(connectivityManager: ConnectivityManager): Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}

