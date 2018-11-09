package br.com.rotacilio.mymoney.commons

import android.content.Context
import android.net.ConnectivityManager

abstract class InternetConnection {

    companion object {
        fun internetAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting
        }
    }
}