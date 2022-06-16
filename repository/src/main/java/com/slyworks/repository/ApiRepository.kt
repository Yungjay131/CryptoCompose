package com.slyworks.repository

import com.slyworks.models.CryptoInfoRequest
import com.slyworks.models.CryptoModel
import io.reactivex.rxjava3.core.Observable

interface ApiRepository : Repository {
    fun getSpecificCryptocurrency(body: CryptoInfoRequest): Observable<List<CryptoModel>>
}