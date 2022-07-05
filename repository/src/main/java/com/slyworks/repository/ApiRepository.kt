package com.slyworks.repository

import com.slyworks.models.CryptoModel
import com.slyworks.models.CryptoModelDetails
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ApiRepository : Repository {

    fun getDataWithFavorites(favorites:List<Int>):Single<List<CryptoModel>>
    fun getSpecificCryptocurrency(query:String): Single<CryptoModelDetails>
    fun getMultipleCryptoInformation(ids:String, favoritesList:List<Int>): Single<List<CryptoModel>>
}