package com.slyworks.repository

import com.slyworks.models.CryptoModel
import com.slyworks.models.CryptoModelDetails
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ApiRepository : Repository {

    fun getAllCryptoInfoMappedWithFavorites(favorites:List<Int>):Single<List<CryptoModel>>
    fun getSpecificCryptoInfo(query:String): Single<CryptoModelDetails>
    fun getSpecificCryptoInfoForID(query:String):Single<CryptoModelDetails>
    fun getMultipleCryptoInfoMappedWithFavorites(ids:String, favoritesList:List<Int>): Single<List<CryptoModel>>
}