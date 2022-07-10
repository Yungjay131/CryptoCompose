package com.slyworks.api

import com.slyworks.models.*
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


/**
 *Created by Joshua Sylvanus, 11:37 AM, 31-May-22.
 */
interface CoinMarketApi {
    companion object {
        /*TODO:move API_KEY server side, maybe Firebase*/
        const val CRYPTO_URL_PATH = "https://s2.coinmarketcap.com/static/img/coins/128x128/%s.png"
        const val URL = "https://pro-api.coinmarketcap.com"
        const val ENDPOINT_FETCH_CRYPTO_DATA =
            "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?limit=100"
        const val API_KEY = "c5edc459-1455-4291-88ff-89fadab8c08a"
        const val AUX_PARAMS = "symbol,name,maxSupply,circulatingSupply,totalSupply,cmcRank," +
                               "lastUpdated,price,marketCap,dateAdded,tags"
    }

    @Headers("X-CMC_PRO_API_KEY:$API_KEY")
    @GET("/v1/cryptocurrency/listings/latest")
    fun getAllCryptoCurrencyInformation(@Query("start") start:Int = 1,
                                        @Query("limit") limit: Int = 100,
                                        @Query("convert") currency: String = "NGN")
    : Single<CryptoEntityGeneral>

    @Headers("X-CMC_PRO_API_KEY:$API_KEY")
    @GET("/v1/cryptocurrency/info")
    fun getSpecificCryptoCurrencyInformation(@Query("slug") query:String)
    : Single<CryptoEntityDetails>

    @Headers("X-CMC_PRO_API_KEY:$API_KEY")
    @GET("/v1/cryptocurrency/quotes/latest")
    fun getMultipleCryptoCurrencyInformation(@Query("id") ids:String,
                                        @Query("convert") currency: String = "NGN")
    : Single<CryptoEntityMultiple>
}

