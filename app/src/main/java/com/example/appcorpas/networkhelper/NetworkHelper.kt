package com.example.appcorpas.networkhelper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Clase de ayuda para saber si tenemos internet o no.
 * Avisa en tiempo real si se conecta o se desconecta el WiFi.
 */
class NetworkHelper(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Esto funciona como un canal que nos manda true o false según la conexión
    val observeNetworkStatus: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true) // Hay internet
            }

            override fun onLost(network: Network) {
                trySend(false) // Se fue internet
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        // Miramos si hay internet nada más arrancar
        trySend(isInternetAvailable(connectivityManager))

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }

    private fun isInternetAvailable(cm: ConnectivityManager): Boolean {
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}