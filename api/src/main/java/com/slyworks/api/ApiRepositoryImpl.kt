package com.slyworks.api

import android.util.Log
import com.slyworks.models.CryptoEntity
import com.slyworks.models.CryptoModel
import com.slyworks.api.CoinMarketApi.Companion.CRYPTO_URL_PATH
import com.slyworks.models.CryptoInfoRequest
import com.slyworks.repository.ApiRepository
import io.reactivex.rxjava3.core.Observable


/**
 *Created by Joshua Sylvanus, 5:20 AM, 04-Jun-22.
 */
class ApiRepositoryImpl constructor(var mInstance:CoinMarketApi): ApiRepository {

    //region Vars
    private val TAG: String? = ApiRepositoryImpl::class.simpleName
    //endregion


    override fun getData(): Observable<List<CryptoModel>> {
        return mInstance.getCryptoInformation()
            .map(::mapResponseToCryptoModel )
            .doOnError {
                Log.e(TAG, "getData: error occurred", it )
            }
    }

    override fun getSpecificCryptocurrency(body:CryptoInfoRequest): Observable<List<CryptoModel>> {
        return mInstance.getSpecificCryptoInformation(body)
              .map(::mapResponseToCryptoModel)
              .doOnError {
                  Log.e(TAG, "getSpecificCryptoCurrency: error occurred", it )
              }
    }

    private fun mapResponseToCryptoModel(c:CryptoEntity)
    :List<CryptoModel>{
        val l:MutableList<CryptoModel> = mutableListOf()

        if(c.data.isNullOrEmpty())
            return emptyList()

        c.data.forEach {
            val e = CryptoModel(
                                id = "",
                                _id = it.id,
                                image = parseImageString(it.id),
                                symbol = it.symbol ?: "",
                                name = it.name ?: "",
                                maxSupply = it.maxSupply,
                                circulatingSupply = it.circulatingSupply,
                                totalSupply = it.totalSupply,
                                cmcRank = it.cmcRank,
                                lastUpdated = it.lastUpdated ?: "",
                                price = it.quote?.ngn?.price ?: 0.0,
                                priceUnit = "₦",
                                marketCap = it.quote?.ngn?.marketCap,
                                dateAdded = it.dateAdded ?: "",
                                tags = parseTagsString(it.tags)
                                )

            l.add(e)
        }

        return l
    }

    private fun parseImageString(entityID:Int?):String = String.format(CRYPTO_URL_PATH,entityID)
    private fun parseTagsString(tags: List<String>?):String{
       if(tags == null) return ""

       var s = ""
       for(i in tags.indices){
         s = "$s,i"
       }

       return s
    }
}