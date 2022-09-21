package com.slyworks.network

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import io.reactivex.rxjava3.core.Observable


/**
 *Created by Joshua Sylvanus, 11:27 AM, 29/05/2022.
 */
@SuppressLint("NewApi")
class NetworkRegister(private var context: Context) {
    //region Vars
    private var mImpl: NetworkWatcher? = null
    //endregion

   init{ init2() }

    private fun init1(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            val  filter = IntentFilter()
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            mImpl = NetworkBroadcastReceiver()
            context.registerReceiver(mImpl as NetworkBroadcastReceiver, filter)
        }else
            mImpl = NetworkWatcherImpl(context)
    }

    private fun init2(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            mImpl = NetworkWatcherLegacyImpl(context)
        else
            mImpl = NetworkWatcherImpl(context)
    }

    /* fuck it!!, just use BroadcastReceiver on every version */
    private fun init3(){
        val  filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        mImpl = NetworkBroadcastReceiver()
        context.registerReceiver(mImpl as NetworkBroadcastReceiver, filter)
    }

    fun getNetworkStatus(): Boolean = mImpl!!.getNetworkStatus()

    fun subscribeToNetworkUpdates():Observable<Boolean>{
        mImpl ?: init2()
        return mImpl!!.subscribeTo()
    }

    fun unsubscribeToNetworkUpdates(){
        mImpl!!.unsubscribeTo()
    }

}