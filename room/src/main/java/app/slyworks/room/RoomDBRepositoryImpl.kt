package app.slyworks.room

import androidx.sqlite.db.SimpleSQLiteQuery
import app.slyworks.room.daos.DataDao
import app.slyworks.room.daos.FavoritesDao
import app.slyworks.room.models.CryptoModelIDRoom
import app.slyworks.room.models.CryptoModelRoom
import com.slyworks.models.CryptoModel
import com.slyworks.repository.DBRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import timber.log.Timber


/**
 * Created by Joshua Sylvanus, 6:39 PM, 17/09/2022.
 */
class RoomDBRepositoryImpl(private val dataDao:DataDao,
                           private val favoritesDao: FavoritesDao) : DBRepository {

    companion object{
        fun mapModelToRoomModel(model: CryptoModel): CryptoModelRoom {
            return CryptoModelRoom(
                _id = model._id,
                image = model.image,
                symbol = model.symbol,
                name = model.name,
                maxSupply = model.maxSupply ?: "0.0",
                circulatingSupply = model.circulatingSupply ?: "0.0",
                totalSupply = model.totalSupply ?: "0.0",
                cmcRank = model.cmcRank,
                lastUpdated = model.lastUpdated,
                price = model.price,
                priceUnit = model.priceUnit,
                marketCap = model.marketCap ?: "0.0",
                dateAdded = model.dateAdded,
                tags = model.tags,
                isFavorite = model.isFavorite )
        }

        fun mapRoomModelToModel(model: CryptoModelRoom): CryptoModel {
            return CryptoModel(
                _id = model._id,
                image = model.image,
                symbol = model.symbol,
                name = model.name,
                maxSupply = model.maxSupply,
                circulatingSupply = model.circulatingSupply,
                totalSupply = model.totalSupply,
                cmcRank = model.cmcRank,
                lastUpdated = model.lastUpdated,
                price = model.price,
                priceUnit = model.priceUnit,
                marketCap = model.marketCap,
                dateAdded = model.dateAdded,
                tags = model.tags,
                isFavorite = model.isFavorite )
        }
    }

    override fun getSpecificData(id:Int, name:String?):Single<CryptoModel>{
        val queryParamString:String =
            if(name != null) "name" else "id"
        val queryParam:Any = name ?: id

        val query:SimpleSQLiteQuery = SimpleSQLiteQuery(
            "SELECT * FROM CryptoModelRoom WHERE $queryParamString = ? LIMIT 1",
            arrayOf<Any>(queryParam) )

        return Single.defer{
            Single.just(dataDao.getSpecificData(query))
                  .map(::mapRoomModelToModel)
        }
    }
    override fun saveData(data: List<CryptoModel>): Completable
    = dataDao.addData(*data.map(::mapModelToRoomModel).toTypedArray())

    override fun getData(favoriteIDs: List<Int>): Single<List<CryptoModel>>
    = dataDao.getDataAsync()
             .first(emptyList())
             .map{ it:List<CryptoModelRoom> ->
                 it.map(::mapRoomModelToModel)
                   .map map1@{ it2:CryptoModel ->
                       if(favoriteIDs.contains(it2._id))
                           it2.isFavorite = true

                       return@map1 it2
                   }
             }
             .onErrorReturn{ t:Throwable ->
                 Timber.e(t.message)
                 return@onErrorReturn emptyList<CryptoModel>()
             }

    override fun getFavorites(): Single<List<Int>>
    = favoritesDao.getFavoritesAsync()
                  .first(emptyList())
                  .map { it:List<CryptoModelIDRoom> -> it.map{ it2:CryptoModelIDRoom -> it2.id } }
                  .onErrorReturn { t:Throwable ->
                      Timber.e(t.message)
                      return@onErrorReturn emptyList<Int>()
                  }

    override fun addToFavorites(vararg data: Int): Completable
    = favoritesDao.addFavorites(*data.map(::CryptoModelIDRoom).toTypedArray())

    override fun removeFromFavorites(vararg data: Int): Completable
    = favoritesDao.deleteFavorite(*data.map(::CryptoModelIDRoom).toTypedArray())
}