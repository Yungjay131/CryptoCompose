package com.slyworks.repository

import com.slyworks.models.CryptoModel
import io.reactivex.rxjava3.core.Observable

interface RealmRepository : Repository {
    fun saveData(data:List<CryptoModel>):Observable<Boolean>
    fun getFavorites():Observable<List<CryptoModel>>
    fun addToFavorites(vararg data:CryptoModel):Observable<Boolean>
    fun removeFromFavorites(vararg data:CryptoModel):Observable<Boolean>
}