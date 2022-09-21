package app.slyworks.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.slyworks.room.daos.DataDao
import app.slyworks.room.daos.FavoritesDao
import app.slyworks.room.models.CryptoModelIDRoom
import app.slyworks.room.models.CryptoModelRoom


/**
 * Created by Joshua Sylvanus, 5:18 PM, 17/09/2022.
 */

@Database(entities = [
    CryptoModelRoom::class,
    CryptoModelIDRoom::class],
    version = 1,
    exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        //region Vars
        private var INSTANCE: AppDatabase? = null;
        //endregion

        @Synchronized
        fun getInstance(context: Context):AppDatabase{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase")
                    .fallbackToDestructiveMigration()
                    .addCallback(object: RoomDatabase.Callback(){})
                    .build()
            }

            return INSTANCE!!
        }
    }


    abstract fun getFavoritesDao(): FavoritesDao
    abstract fun getDataDao(): DataDao
}