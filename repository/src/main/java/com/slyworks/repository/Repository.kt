package com.slyworks.repository

import com.slyworks.models.CryptoModel
import io.reactivex.rxjava3.core.Observable


/**
 *Created by Joshua Sylvanus, 5:09 AM, 04-Jun-22.
 */
interface Repository {
    fun getData(): Observable<List<CryptoModel>>
}

