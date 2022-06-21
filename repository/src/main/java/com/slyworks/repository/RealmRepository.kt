package com.slyworks.repository

import com.slyworks.models.CryptoModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface RealmRepository : Repository {
    fun saveData(data:List<CryptoModel>):Completable
    fun getFavorites(): Single<List<CryptoModel>>
    fun addToFavorites(vararg data:CryptoModel):Completable
    fun removeFromFavorites(vararg data:CryptoModel):Completable
}