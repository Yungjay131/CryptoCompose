package com.slyworks.data

import com.slyworks.models.CryptoModel
import com.slyworks.models.CryptoModelCombo
import com.slyworks.models.CryptoModelDetails
import com.slyworks.models.Outcome
import com.slyworks.network.NetworkRegister
import com.slyworks.repository.ApiRepository
import com.slyworks.repository.DBRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit


/**
 * Created by Joshua Sylvanus, 2:42 PM, 04-Jun-22.
 */
enum class SpecificCryptoSearchType{ DETAILS, SEARCH }
class DataManager
constructor(private val mNetworkRegister:NetworkRegister,
            private val mRepo1: ApiRepository,
            private val mRepo2: DBRepository){
    //region Vars
    private val TAG: String? = DataManager::class.simpleName
    //endregion

    fun getNetworkStatus():Boolean = mNetworkRegister.getNetworkStatus()

    fun observeNetworkStatus():Observable<Boolean>
    = mNetworkRegister.subscribeToNetworkUpdates()

    fun getSpecificCryptocurrency(query: String,
                                  type: SpecificCryptoSearchType):Observable<Outcome>
    = Observable.just(query)
        .flatMapSingle {
            when(type){
                SpecificCryptoSearchType.SEARCH -> mRepo1.getSpecificCryptoInfo(query)
                SpecificCryptoSearchType.DETAILS -> mRepo1.getSpecificCryptoInfoForID(query)
            }
        }
        .flatMap { it2:CryptoModelDetails? ->
            if(it2 == null)
            /*means there was a error, or it probably doesn't exist*/
                Observable.just(Outcome.FAILURE<String>(value = "crypto-currency not found"))
            else{
                /*means it exist hence get associated data*/
                val o:Observable<CryptoModel> =
                mRepo2.getFavorites()
                      .toObservable()
                      .flatMapSingle { it3:List<Int> ->
                        mRepo1.getMultipleCryptoInfoMappedWithFavorites(query, it3)
                              .map{ it4:List<CryptoModel> -> it4.first() }
                      }

                return@flatMap o.zipWith(Observable.just<CryptoModelDetails>(it2))
                { model: CryptoModel, details: CryptoModelDetails ->
                      Outcome.SUCCESS<CryptoModelCombo>(CryptoModelCombo(model = model, details = details))
                }
            }
        }

    fun getData():Observable<Outcome>
    = Observable.just(mNetworkRegister.getNetworkStatus())
                .flatMap {
                    if(it){
                          mRepo2.getFavorites()
                                .flatMapObservable { it2: List<Int> ->
                                    mRepo1.getAllCryptoInfoMappedWithFavorites(it2)
                                        .toObservable()
                                        .flatMap { it3: List<CryptoModel> ->
                                            mRepo2.saveData(it3)
                                                  .andThen(
                                                      Observable.just(Outcome.SUCCESS<List<CryptoModel>>(value = it3))
                                                  )
                                        }
                                }
                                .repeatWhen{ it4:Observable<Any> ->
                                   it4.flatMap { _:Any -> Observable.interval(2, TimeUnit.MINUTES) }
                                      .repeatUntil{ !mNetworkRegister.getNetworkStatus() }
                                }
                    }else{
                        mRepo2.getData()
                            .flatMapObservable flatMap1@ { it4:List<CryptoModel> ->
                                if(it4.isNullOrEmpty())
                                    return@flatMap1 Observable.just(Outcome.FAILURE<String>(value = "you are currently not"+
                                            " connected to the internet and have no data saved offline"))

                                Observable.just(Outcome.SUCCESS(value = it4))
                            }
                    }
                }

    fun getData2():Observable<Outcome>{
        return mNetworkRegister.subscribeToNetworkUpdates()
            .switchMap {
               if(it){
                   Observable.interval(0, 5, TimeUnit.MINUTES)
                       //.startWithItem(0)
                       .switchMap {
                           mRepo2.getFavorites()
                                 .toObservable()
                                 .switchMap { it2:List<Int> ->
                                     mRepo1.getAllCryptoInfoMappedWithFavorites(it2)
                                           .toObservable()
                                           .flatMap { it3:List<CryptoModel> ->
                                               mRepo2.saveData(it3)
                                                     .andThen(
                                                         Observable.just(Outcome.SUCCESS<List<CryptoModel>>(value = it3))
                                                     )
                                           }
                                 }
                       }
               }else{
                   mRepo2.getData()
                       .toObservable()
                       .flatMap { it4:List<CryptoModel> ->
                           if(it4.isNullOrEmpty())
                               return@flatMap Observable.just(Outcome.FAILURE<String>(value = "you are currently not"+
                                       " connected to the internet and have no data saved offline"))

                           Observable.just(Outcome.SUCCESS(value = it4))
                       }
               }
            }
    }

    fun addToFavorites(vararg data:Int):Completable
    = mRepo2.addToFavorites(*data)

    fun removeFromFavorites(vararg data:Int):Completable
    =  mRepo2.removeFromFavorites(*data)

    fun getFavorites():Observable<Outcome>
    = Observable.just(mNetworkRegister.getNetworkStatus())
                .flatMap { it:Boolean ->
                    if(!it){
                        Observable.just(Outcome.ERROR<Unit>(additionalInfo = "you are currently not connected to the internet."+
                                " Please check your connection and try again"))
                    }else{
                        mRepo2.getFavorites()
                            .toObservable()
                            .flatMap flatMap1@{it2:List<Int> ->
                                if(it2.isNullOrEmpty()){
                                    return@flatMap1 Observable.just(
                                        Outcome.FAILURE<Unit>(additionalInfo = "you have no favorites at the moment."+
                                                "Check the \"Favorite\" icon on an item to add it to your Favorites"))
                                }

                                val s:StringBuilder = StringBuilder()
                                for(i in it2.indices){
                                    if(i != it2.size - 1)
                                        s.append("${it2[i]},")
                                    else
                                        s.append("${it2[i]}")
                                }

                                mRepo1.getMultipleCryptoInfoMappedWithFavorites(s.toString(), it2)
                                    .toObservable()
                                    .switchMap { it3:List<CryptoModel> ->
                                        Observable.just(Outcome.SUCCESS<List<CryptoModel>>(value = it3, additionalInfo = "successful"))
                                    }
                            }
                    }
                }

    fun getFavorites2():Observable<Outcome>
     = mNetworkRegister.subscribeToNetworkUpdates()
          .switchMap { it ->
              if(!it){
                  Observable.just(Outcome.ERROR<Unit>(additionalInfo = "you are currently not connected to the internet."+
                          " Please check your connection and try again"))
              }else{
                  mRepo2.getFavorites()
                        .toObservable()
                        .switchMap switchMap1@{it2:List<Int> ->
                             if(it2.isNullOrEmpty()){
                                 return@switchMap1 Observable.just(
                                     Outcome.FAILURE<Unit>(additionalInfo = "you have no favorites at the moment."+
                                         "Check the \"Favorite\" icon on an item to add it to your Favorites"))
                             }

                            val s:StringBuilder = StringBuilder()
                            for(i in it2.indices){
                                if(i != it2.size - 1)
                                    s.append("${it2[i]},")
                                else
                                    s.append("${it2[i]}")
                            }

                           mRepo1.getMultipleCryptoInfoMappedWithFavorites(s.toString(), it2)
                                 .toObservable()
                                 .switchMap { it3:List<CryptoModel> ->
                                     Observable.just(Outcome.SUCCESS<List<CryptoModel>>(value = it3, additionalInfo = "successful"))
                                 }
                        }
              }
          }

}