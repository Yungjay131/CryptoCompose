package com.slyworks.data

import android.util.Log
import com.slyworks.models.CryptoInfoRequest
import com.slyworks.models.CryptoModel
import com.slyworks.models.Outcome
import com.slyworks.network.NetworkRegister
import com.slyworks.repository.ApiRepository
import com.slyworks.repository.RealmRepository
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Named


/**
 *Created by Joshua Sylvanus, 2:42 PM, 04-Jun-22.
 */
class DataManager
constructor(private val mNetworkRegister:NetworkRegister,
            private val mRepo1: ApiRepository,
            private val mRepo2: RealmRepository){

    private val TAG: String? = DataManager::class.simpleName

    init{
        /*checking how many instances of DataManager is created,(expecting it to be a Singleton)*/
        Log.e(TAG, "DataManager: a new DataManager instance is created")
    }

    fun getSpecificCryptocurrency(name:String):Observable<List<CryptoModel>>{
        return mRepo1.getSpecificCryptocurrency(CryptoInfoRequest(name = name))
    }

    fun getData(): Observable<List<CryptoModel>> {
        return mNetworkRegister
            .subscribeToNetworkUpdates()
           /* .doOnSubscribe {
                mNetworkRegister.emitInitialNetworkStatus()
            }*/
            .flatMap {
                if(it) {
                    /*make the network call every 3 minutes*/
                    Observable.interval(3, TimeUnit.MINUTES)
                               .flatMap { _ ->
                                   mRepo1.getData()
                                         .flatMap { l ->
                                              mRepo2.saveData(l)
                                                    .flatMap { _ -> Observable.just(l) }
                                         }
                               }
                }else{
                    mRepo2.getData()
                }

            }


    }

    fun getData2():Observable<Outcome>{
        return mNetworkRegister
            .subscribeToNetworkUpdates()
            .doOnSubscribe {
                mNetworkRegister.emitInitialNetworkStatus()
            }
            .flatMap {
                if(it){
                    Observable.interval(3, TimeUnit.MINUTES)
                        .flatMap { _ ->
                            mRepo1.getData()
                                .flatMap { l ->
                                    mRepo2.saveData(l)
                                        .flatMap { _ ->
                                            Observable.just(Outcome.SUCCESS(value = l))
                                        }
                                }
                        }
                }else{
                    mRepo2.getData()
                        .map { l ->
                            if(l.isNullOrEmpty())
                                Outcome.FAILURE(value = null, reason = "there is no network connectivity"+
                                        "and you currently have no offline data")
                            else
                                Outcome.SUCCESS(value = l, additionalInfo = "loaded offline cached data successfully")

                        }
                }
            }
    }

    fun addToFavorites(vararg data:CryptoModel):Observable<Boolean>
    = mRepo2.addToFavorites(*data)


    fun removeFromFavorites(vararg data:CryptoModel):Observable<Boolean>
    =  mRepo2.removeFromFavorites(*data)


    fun getFavorites():Observable<List<CryptoModel>>
    =  mRepo2.getFavorites()


}