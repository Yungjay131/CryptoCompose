package com.slyworks.network.di

import android.content.Context
import com.slyworks.di.ApplicationScope
import com.slyworks.network.NetworkRegister
import dagger.Module
import dagger.Provides


/**
 *Created by Joshua Sylvanus, 3:53 PM, 14-Jun-22.
 */
@Module
object NetworkModule {

    @Provides
    @ApplicationScope
    fun provideNetworkRegister(context: Context): NetworkRegister {
        return NetworkRegister(context)
    }
}