package app.slyworks.room.di

import android.content.Context
import app.slyworks.room.AppDatabase
import app.slyworks.room.RoomDBRepositoryImpl
import app.slyworks.room.daos.DataDao
import app.slyworks.room.daos.FavoritesDao
import com.slyworks.di.ApplicationScope
import com.slyworks.repository.DBRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named


/**
 *Created by Joshua Sylvanus, 6:23 PM, 17/09/2022.
 */

@Module
object RoomModule {
    @Provides
    @ApplicationScope
    fun provideAppDatabase(context: Context):AppDatabase
    = AppDatabase.getInstance(context)

    @Provides
    @ApplicationScope
    fun provideDataDao(appDatabase: AppDatabase):DataDao
    = appDatabase.getDataDao()

     @Provides
    @ApplicationScope
    fun provideFavoritesDao(appDatabase: AppDatabase):FavoritesDao
    = appDatabase.getFavoritesDao()

    @Provides
    @ApplicationScope
    @Named("room_db_repository_impl")
    fun provideRoomDBRepository(dataDao: DataDao,
                                favoritesDao: FavoritesDao): DBRepository
    = RoomDBRepositoryImpl(dataDao, favoritesDao)
}