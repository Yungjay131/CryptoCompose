package com.slyworks.data.di

import app.slyworks.room.di.RoomModule
import com.slyworks.di.ApplicationScope
import com.slyworks.api.ApiRepositoryImpl
import com.slyworks.api.di.ApiModule
import com.slyworks.data.DataManager
import com.slyworks.network.NetworkRegister
import com.slyworks.network.di.NetworkModule
import com.slyworks.realm.RealmDBRepositoryImpl
import com.slyworks.realm.di.RealmModule
import com.slyworks.repository.ApiRepository
import com.slyworks.repository.DBRepository
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
        RoomModule::class,
        ApiModule::class,
        NetworkModule::class
    ]
)
object DataModule {
    @Provides
    @ApplicationScope
    fun provideDataManager(register:NetworkRegister,
                           repo1: ApiRepository,
                           @Named("realm_db_repository_impl")
                           repo2: DBRepository): DataManager {
        return DataManager(register,repo1,repo2)
    }

    @Module
    interface Bindings{
        @Binds
        @Named("network")
        @ApplicationScope
        fun bindNetworkRepository(impl: ApiRepositoryImpl): ApiRepository

        @Binds
        @Named("realm")
        @ApplicationScope
        fun bindRealmRepository(impl: RealmDBRepositoryImpl): DBRepository
    }
}