package app.slyworks.room.daos

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import app.slyworks.room.models.CryptoModelRoom
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable


/**
 *Created by Joshua Sylvanus, 6:07 PM, 17/09/2022.
 */
@Dao
interface DataDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun addData(vararg data: CryptoModelRoom):Completable

  @Query("DELETE FROM CryptoModelRoom")
  fun deleteData():Completable

  @Query("SELECT * FROM CryptoModelRoom")
  fun observeData(): Flowable<List<CryptoModelRoom>>

  @Query("SELECT * FROM CryptoModelRoom")
  fun getDataAsync():Observable<List<CryptoModelRoom>>

  @Query("SELECT * FROM CryptoModelRoom")
  fun getData():List<CryptoModelRoom>

  @Query("SELECT COUNT(id) FROM CryptoModelRoom")
  fun getDataCountAsync():Observable<Int>

  @Query("SELECT COUNT(id) FROM CryptoModelRoom")
  fun getDataCount():Int

  @RawQuery
  fun getSpecificData(query: SupportSQLiteQuery): CryptoModelRoom
}