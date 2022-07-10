package com.slyworks.data

import com.slyworks.models.CryptoModel
import com.slyworks.models.CryptoModelCombo
import com.slyworks.models.CryptoModelDetails
import com.slyworks.network.NetworkRegister
import com.slyworks.repository.ApiRepository
import com.slyworks.repository.RealmRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit


/**
 *Created by Joshua Sylvanus, 2:42 PM, 04-Jun-22.
 */
class DataManager
constructor(private val mNetworkRegister:NetworkRegister,
            private val mRepo1: ApiRepository,
            private val mRepo2: RealmRepository){

    private val TAG: String? = DataManager::class.simpleName

    fun getNetworkStatus():Boolean = mNetworkRegister.getNetworkStatus()

    fun observeNetworkStatus():Observable<Boolean> =
        Observable.merge(
            Observable.just(mNetworkRegister.getNetworkStatus()),
            mNetworkRegister.subscribeToNetworkUpdates())

    fun getSpecificCryptocurrency(query:String):Observable<CryptoModelCombo>
    /*TODO:maybe use zip() here*/
       = mRepo1.getSpecificCryptoInfoMappedWithFavorites(query)
            .toObservable()
            .flatMap { c ->
                if(c != null)
                    /*means there was a error, probably doesn't exist*/
                    Observable.just<CryptoModelCombo>(CryptoModelCombo.empty())
                else{
                    /*means it exist hence get associated data*/
                    Observable.combineLatest(
                        Observable.just<CryptoModelDetails>(c),
                        mRepo2.getFavorites()
                              .toObservable()
                              .flatMap { it:List<Int> ->
                                  mRepo1.getMultipleCryptoInfoMappedWithFavorites(query, it)
                                      .toObservable()
                                      .map { it2:List<CryptoModel> ->
                                          it2.first()
                                      }
                              },
                        { details:CryptoModelDetails, model:CryptoModel ->
                            CryptoModelCombo(
                                model = model,
                                details = details)
                        }
                    )
                }
            }


    fun getData(): Observable<List<CryptoModel>>  =
        mNetworkRegister.subscribeToNetworkUpdates()
          .flatMap {
                if(it) {
                    /*make the network call every 10 minutes*/
                    Observable.merge(
                        Observable.just(0),
                        Observable.interval(10, TimeUnit.MINUTES)
                        )
                        .flatMap { _ ->
                            mRepo2.getFavorites()
                                .toObservable()
                                .flatMap { it2:List<Int> ->
                                    mRepo1.getAllCryptoInfoMappedWithFavorites(it2)
                                        .toObservable()
                                        .flatMap { l:List<CryptoModel> ->
                                            mRepo2.saveData(l)
                                                .andThen(Observable.just(l))
                                        }
                                }

                        }
                }else{
                    mRepo2.getData()
                          .toObservable()
                }

            }




    fun addToFavorites(vararg data:Int):Completable
    = mRepo2.addToFavorites(*data)


    fun removeFromFavorites(vararg data:Int):Completable
    =  mRepo2.removeFromFavorites(*data)


    fun getFavorites(): Observable<Triple<List<CryptoModel>,Int, String?>>
    =  Observable.merge(
                 Observable.just(mNetworkRegister.getNetworkStatus()),
                 mNetworkRegister.subscribeToNetworkUpdates()
              ).flatMap {
                      if(!it)
                         Observable.just(
                             Triple(
                             emptyList<CryptoModel>(), 1,"you are currently not connected to the internet."+
                                     " Please check your connection and try again")
                         )
                      else
                          mRepo2.getFavorites()
                              .toObservable()
                              .flatMap flatMap2@{ it2:List<Int> ->
                                  if(it2.isNullOrEmpty())
                                      return@flatMap2 Observable.just(
                                          Triple(
                                          emptyList<CryptoModel>() ,2,"you have no favorites at the moment."+
                                          "Check the \"Favorite\" icon to add an item to your Favorites")
                                      )


                                  val s:StringBuilder = StringBuilder()
                                  for(i in it2.indices){
                                      if(i != it2.size - 1)
                                          s.append("$i,")
                                      else
                                          s.append("$i")
                                  }

                                  mRepo1.getMultipleCryptoInfoMappedWithFavorites(s.toString(), it2)
                                      .toObservable()
                                      .flatMap { it4:List<CryptoModel> ->
                                          Observable.just(Triple(it4 ,3, "successful"))
                                      }
                              }

              }


}