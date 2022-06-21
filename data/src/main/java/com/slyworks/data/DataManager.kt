package com.slyworks.data

import android.util.Log
import com.slyworks.models.CryptoInfoRequest
import com.slyworks.models.CryptoModel
import com.slyworks.models.CryptoModelDetails
import com.slyworks.models.Outcome
import com.slyworks.network.NetworkRegister
import com.slyworks.repository.ApiRepository
import com.slyworks.repository.RealmRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
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

    fun getSpecificCryptocurrency(query:String):Observable<CryptoModelDetails>
    = mRepo1.getSpecificCryptocurrency(query)
            .toObservable()

    fun getData(): Observable<List<CryptoModel>> {
        return Observable.merge(
            Observable.just(mNetworkRegister.getNetworkStatus()),
            mNetworkRegister.subscribeToNetworkUpdates()
        ).flatMap {
                if(it) {
                    /*make the network call every 3 minutes*/
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


    fun addToFavorites(vararg data:CryptoModel):Completable
    = mRepo2.addToFavorites(*data)


    fun removeFromFavorites(vararg data:CryptoModel):Completable
    =  mRepo2.removeFromFavorites(*data)


    fun getFavorites(): Single<List<CryptoModel>>
    =  mRepo2.getFavorites()


}