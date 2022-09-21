package com.slyworks.realm

import com.slyworks.models.CryptoModel
import com.slyworks.repository.DBRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 5:19 AM, 04-Jun-22.
 */
class RealmDBRepositoryImpl(private val config: RealmConfiguration) : DBRepository {
    //region Vars
    private val TAG: String? = RealmDBRepositoryImpl::class.simpleName

    private val schedulers_realm:Scheduler = Schedulers.single()
    //endregion

    companion object{
        fun mapModelToRealmModel(model: CryptoModel): CryptoModelRealm {
            return CryptoModelRealm(
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

        fun mapRealmModelToModel(model: CryptoModelRealm): CryptoModel {
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

    override fun getSpecificData(id: Int, name: String?): Single<CryptoModel> {
        return Single.error(Exception("not implemented"))
    }

    override fun getData(favoriteIDs:List<Int>): Single<List<CryptoModel>>
       = Single.create<List<CryptoModel>>{ emitter ->
           Realm.getInstance(config)
               .executeTransaction(Realm.Transaction {
               val l:List<CryptoModel>
               = it.where(CryptoModelRealm::class.java)
                   .findAll()
                   .sort("cmcRank", Sort.ASCENDING)
                   .map(::mapRealmModelToModel)
                   .map{ it2:CryptoModel ->
                       if(favoriteIDs.contains(it2._id))
                           it2.isFavorite = true

                       return@map it2
                   }

               emitter.onSuccess(l)
           })
       }.subscribeOn(schedulers_realm)


    override fun saveData(data: List<CryptoModel>): Completable
        = Completable.create { emitter ->
            try {
                val l: List<CryptoModelRealm> =
                    data.toMutableList()
                        .map(::mapModelToRealmModel)

                Realm.getInstance(config)
                    .executeTransaction(Realm.Transaction {
                    it.insertOrUpdate(l)

                    emitter.onComplete()
                })
            } catch (e: Exception) {
                Timber.e(e, "saveData: error occurred")
                emitter.onError(e)
            }
        }.subscribeOn(schedulers_realm)


    override fun getFavorites(): Single<List<Int>>
      = Single.create<List<Int>> { emitter ->
            Realm.getInstance(config)
                .executeTransaction(Realm.Transaction {
                val l:List<CryptoModelID> = it.where(CryptoModelID::class.java)
                    .findAll()
                    .sort("id", Sort.ASCENDING)

                val r:List<Int> = l.map { it2 -> it2.id }
                emitter.onSuccess(r)
            })
        }.subscribeOn(schedulers_realm)


    override fun addToFavorites(vararg data: Int): Completable
      = Completable.create { emitter ->
            try {
                val l: List<CryptoModelID> =
                    data.toList()
                        .map{ CryptoModelID(id = it) }

                 Realm.getInstance(config)
                     .executeTransaction {
                      it.insertOrUpdate(l)
                      it.close()

                      emitter.onComplete()
                }
            } catch (e: Exception) {
                Timber.e("addToFavorites: error occurred")
                emitter.onError(e)
            }
        }.subscribeOn(schedulers_realm)


    override fun removeFromFavorites(vararg data: Int): Completable
      = Completable.create { emitter ->
            try {
                val l: List<CryptoModelID> =
                    data.toList()
                        .map{ CryptoModelID(id = it) }

                 Realm.getInstance(config)
                     .executeTransaction {
                    l.forEach { i ->
                        it.where(CryptoModelID::class.java)
                            .equalTo("id", i.id)
                            .findFirst()
                            ?.deleteFromRealm()
                    }
                   it.close()

                   emitter.onComplete()
                }


            } catch (e: Exception) {
                Timber.e("removeFromFavorites: error occurred")
                emitter.onError(e)
            }
        }.subscribeOn(schedulers_realm)


    fun deleteData(name:String){
         Realm.getInstance(config)
             .where(CryptoModelRealm::class.java)
            .equalTo("name", name)
            .findFirst()
            ?.deleteFromRealm()
    }
}