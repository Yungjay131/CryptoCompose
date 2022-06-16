package com.slyworks.data.di

import com.slyworks.di.ApplicationScope
import com.slyworks.api.CoinMarketApi
import com.slyworks.api.ApiRepositoryImpl
import com.slyworks.api.di.ApiModule
import com.slyworks.data.DataManager
import com.slyworks.network.NetworkRegister
import com.slyworks.network.di.NetworkModule
import com.slyworks.realm.RealmRepositoryImpl
import com.slyworks.realm.di.RealmModule
import com.slyworks.repository.ApiRepository
import com.slyworks.repository.RealmRepository
import com.slyworks.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named


/**
 *Created by Joshua Sylvanus, 8:49 PM, 09-Jun-22.
 */

@Module(
    includes = [
        RealmModule::class,
        ApiModule::class,
        NetworkModule::class
    ]
)
object DataModule {
    @Provides
    @ApplicationScope
    fun provideDataManager(register:NetworkRegister,
                           repo1: ApiRepository,
                           repo2: RealmRepository
    ): DataManager {
        return DataManager(register,repo1,repo2)
    }

    @Module
    interface Bindings{
        @Binds
        @Named("network")
        @ApplicationScope
        fun bindNetworkRepository(impl: ApiRepositoryImpl): Repository

        @Binds
        @Named("realm")
        @ApplicationScope
        fun bindRealmRepository(impl: RealmRepositoryImpl): Repository

    }
}