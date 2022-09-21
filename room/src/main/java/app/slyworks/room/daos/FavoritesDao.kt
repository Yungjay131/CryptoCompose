package app.slyworks.room.daos

import androidx.room.*
import app.slyworks.room.models.CryptoModelIDRoom
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable


/**
 *Created by Joshua Sylvanus, 5:28 PM, 17/09/2022.
 */
@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorites(vararg favoriteIDs: CryptoModelIDRoom): Completable

    @Delete
    fun deleteFavorite(vararg favoriteIDs: CryptoModelIDRoom):Completable

    @Query("SELECT * FROM CryptoModelIDRoom")
    fun observeFavorites(): Flowable<List<CryptoModelIDRoom>>

    @Query("SELECT * FROM CryptoModelIDRoom")
    fun getFavoritesAsync():Observable<List<CryptoModelIDRoom>>

    @Query("SELECT * FROM CryptoModelIDRoom")
    fun getFavorites():List<CryptoModelIDRoom>

    @Query("SELECT COUNT(id) FROM CryptoModelIDRoom")
    fun getFavoriteCountAsync():Observable<Int>

    @Query("SELECT COUNT(id) FROM CryptoModelIDRoom")
    fun getFavoriteCount():Int
}