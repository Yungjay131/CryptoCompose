package com.slyworks.realm

import com.slyworks.models.CryptoModel
import com.slyworks.repository.RealmRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 5:19 AM, 04-Jun-22.
 */
class RealmRepositoryImpl(private val config: RealmConfiguration) : RealmRepository {
    //region Vars
    private val TAG: String? = RealmRepositoryImpl::class.simpleName
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

    override fun getData(): Single<List<CryptoModel>> {
       return Single.create{ emitter ->
           Realm.getInstance(config)
               .executeTransaction(Realm.Transaction {
               val l:List<CryptoModel> = it.where(CryptoModelRealm::class.java)
                   .findAll()
                   .sort("cmcRank", Sort.ASCENDING)
                   .map(::mapRealmModelToModel)

               emitter.onSuccess(l)
           })
       }
    }

    override fun saveData(data: List<CryptoModel>): Completable {
        return Completable.create { emitter ->
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
        }
    }

    override fun getFavorites(): Single<List<Int>> {
        return Single.create { emitter ->
            Realm.getInstance(config)
                .executeTransaction(Realm.Transaction {
                val l:List<CryptoModelID> = it.where(CryptoModelID::class.java)
                    .findAll()
                    .sort("id", Sort.ASCENDING)

                val r:List<Int> = l.map { it2 -> it2.id }
                emitter.onSuccess(r)
            })
        }
    }

    override fun addToFavorites(vararg data: Int): Completable {
        return Completable.create { emitter ->

            try {
                val l: List<CryptoModelID> =
                    data.toList()
                        .map{ CryptoModelID(id = it) }

                 Realm.getInstance(config)
                     .executeTransaction {
                      it.insertOrUpdate(l)

                    emitter.onComplete()
                }
            } catch (e: Exception) {
                Timber.e("addToFavorites: error occurred")
                emitter.onError(e)
            }
        }
    }

    override fun removeFromFavorites(vararg data: Int): Completable {
        return Completable.create { emitter ->

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

                   emitter.onComplete()
                }


            } catch (e: Exception) {
                Timber.e("removeFromFavorites: error occurred")
                emitter.onError(e)
            }
        }
    }

    fun deleteData(name:String){
         Realm.getInstance(config)
             .where(CryptoModelRealm::class.java)
            .equalTo("name", name)
            .findFirst()
            ?.deleteFromRealm()
    }
}