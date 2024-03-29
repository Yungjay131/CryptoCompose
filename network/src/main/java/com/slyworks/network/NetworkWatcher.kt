package com.slyworks.network

import android.app.Activity
import io.reactivex.rxjava3.core.Observable

/**
 *Created by Joshua Sylvanus, 5:19 PM, 1/13/2022.
 */
interface NetworkWatcher{
    fun getNetworkStatus():Boolean
    fun subscribeTo(): Observable<Boolean>
    fun unsubscribeTo()
}