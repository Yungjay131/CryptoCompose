package com.slyworks.data

import com.slyworks.models.CryptoModel
import com.slyworks.models.CryptoModelCombo
import com.slyworks.models.CryptoModelDetails
import com.slyworks.network.NetworkRegister
import com.slyworks.repository.ApiRepository
import com.slyworks.repository.RealmRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
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

    fun getSpecificCryptocurrency(query:String):Observable<CryptoModelCombo?>
       = mRepo1.getSpecificCryptocurrency(query)
            .toObservable()
            .flatMap { c ->
                if(c != null)
                    /*means there was a error, probably doesn't exist*/
                    Observable.just<CryptoModelCombo>(null)
                else{
                    /*means it exist hence get associated data*/
                    Observable.combineLatest(
                        Observable.just<CryptoModelDetails>(c),
                        mRepo1.getMultipleCryptoInformation(query)
                              .toObservable(),
                        { details: CryptoModelDetails, model: CryptoModel ->
                            CryptoModelCombo(
                                model = model,
                                details = details)
                        }
                    )
                }
            }


    fun getData(): Observable<List<CryptoModel>> {
        return Observable.merge(
            Observable.just(mNetworkRegister.getNetworkStatus()),
            mNetworkRegister.subscribeToNetworkUpdates()
        ).flatMap {
                if(it) {
                    /*make the network call every 10 minutes*/
                    Observable.merge(
                        Observable.just(0),
                        Observable.interval(10, TimeUnit.MINUTES)
                        )
                        .flatMap { _ ->
                            mRepo1.getData()
                                  .toObservable()
                                  .flatMap { l ->
                                     mRepo2.saveData(l)
                                           .andThen(Observable.just(l))
                                  }
                        }
                }else{
                    mRepo2.getData()
                          .toObservable()
                }

            }


    }


    fun addToFavorites(vararg data:Int):Completable
    = mRepo2.addToFavorites(*data)


    fun removeFromFavorites(vararg data:Int):Completable
    =  mRepo2.removeFromFavorites(*data)


    fun getFavorites(): Observable<List<CryptoModel>>
    =  Observable.merge(
                 Observable.just(mNetworkRegister.getNetworkStatus()),
                 mNetworkRegister.subscribeToNetworkUpdates()
              ).flatMap {
                      if(!it)
                       Observable.just(emptyList<CryptoModel>())
                      else
                          mRepo2.getFavorites()
                              .toObservable()
                              .map { it2 ->
                                  val s:StringBuilder = StringBuilder()
                                  for(i in it2.indices){
                                      if(i != it2.size - 1)
                                          s.append("$i,")
                                      else
                                          s.append("$i")
                                  }

                                  s.toString()
                              }
                              .flatMap {
                                  mRepo1.getData()
                                        .toObservable()
                              }
              }


}