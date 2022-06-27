package com.slyworks.repository

import com.slyworks.models.CryptoModel
import com.slyworks.models.CryptoModelDetails
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface ApiRepository : Repository {
    fun getSpecificCryptocurrency(query:String): Single<CryptoModelDetails>
    fun getMultipleCryptoInformation(ids:String): Single<CryptoModel>
}