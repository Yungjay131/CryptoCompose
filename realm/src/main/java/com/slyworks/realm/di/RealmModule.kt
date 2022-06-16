package com.slyworks.realm.di

import com.slyworks.di.ApplicationScope
import com.slyworks.realm.RealmRepositoryImpl
import com.slyworks.repository.RealmRepository
import dagger.Module
import dagger.Provides
import io.realm.RealmConfiguration


/**
 *Created by Joshua Sylvanus, 10:42 AM, 11-Jun-22.
 */
@Module
object RealmModule {

    @Provides
    @ApplicationScope
    fun provideRealmRepository(config: RealmConfiguration):RealmRepository{
        return RealmRepositoryImpl(config)
    }
}