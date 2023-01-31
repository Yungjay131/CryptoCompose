package com.slyworks.realm.di

import com.slyworks.di.ApplicationScope
import com.slyworks.realm.RealmDBRepositoryImpl
import com.slyworks.repository.DBRepository
import dagger.Module
import dagger.Provides
import io.realm.RealmConfiguration
import javax.inject.Named


/**
 *Created by Joshua Sylvanus, 10:42 AM, 11-Jun-22.
 */
@Module
object RealmModule {

    @Provides
    @ApplicationScope
    @Named("realm_db_repository_impl")
    fun provideRealmDBRepository(config: RealmConfiguration):DBRepository{
        return RealmDBRepositoryImpl(config)
    }
}