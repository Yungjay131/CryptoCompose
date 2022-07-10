package com.slyworks.api

import com.slyworks.api.CoinMarketApi.Companion.CRYPTO_URL_PATH
import com.slyworks.models.*
import com.slyworks.repository.ApiRepository
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.text.DecimalFormat


/**
 *Created by Joshua Sylvanus, 5:20 AM, 04-Jun-22.
 */
class ApiRepositoryImpl constructor(var mInstance:CoinMarketApi): ApiRepository {

    //region Vars
    private val TAG: String? = ApiRepositoryImpl::class.simpleName

    private lateinit var mFavoritesList:List<Int>
    //endregion

    override fun getData():Single<List<CryptoModel>> =
        getAllCryptoInfoMappedWithFavorites(emptyList())

    override fun getAllCryptoInfoMappedWithFavorites(favorites: List<Int>): Single<List<CryptoModel>> {
        mFavoritesList = favorites

        return mInstance.getAllCryptoCurrencyInformation()
            .map{
                val l = mutableListOf<CryptoModel>()

                it.data.forEach { e:CryptoEntityGeneral.CryptoCurrencyGeneral ->
                    val model:CryptoModel = CryptoModel(
                        _id = e._id,
                        image = parseImageString(e._id),
                        symbol = e.symbol,
                        name = e.name,
                        maxSupply = e.maxSupply.parseDouble(),
                        circulatingSupply = e.circulatingSupply.parseDouble(),
                        totalSupply = e.totalSupply.parseDouble(),
                        cmcRank = e.cmcRank,
                        lastUpdated = e.lastUpdated,
                        price = e.quote.ngn.price,
                        priceUnit = "",
                        marketCap = e.quote.ngn.marketCap.parseDouble(),
                        dateAdded = e.dateAdded,
                        tags = parseTagsString(e.tags),
                        isFavorite = mFavoritesList.contains(e._id)
                    )

                    l.add(model)
                }

                return@map l as List<CryptoModel>
            }
            .doOnError {
                Timber.e(it, "getData: error occurred")
            }
    }

    override fun getMultipleCryptoInfoMappedWithFavorites(ids: String, favoritesList:List<Int>): Single<List<CryptoModel>> {
        mFavoritesList = favoritesList

        return mInstance.getMultipleCryptoCurrencyInformation(ids)
            .map{
                val l = mutableListOf<CryptoModel>()

                it.keyList.forEach { i ->
                    val e:CryptoEntityMultiple.CryptoCurrencyMultiple = it.data.get(i)!!
                    val model:CryptoModel = CryptoModel(
                        _id = e.id,
                        image = parseImageString(e.id),
                        symbol = e.symbol,
                        name = e.name,
                        maxSupply = e.maxSupply.parseDouble(),
                        circulatingSupply = e.circulatingSupply.parseDouble(),
                        totalSupply = e.totalSupply.parseDouble(),
                        cmcRank = e.cmcRank,
                        lastUpdated = e.lastUpdated,
                        price = e.quote.nGN.price,
                        priceUnit = "",
                        marketCap = e.quote.nGN.marketCap.parseDouble(),
                        dateAdded = e.dateAdded,
                        tags = parseTagsString(e.tags),
                        isFavorite = mFavoritesList.contains(e.id)
                    )
                }

                return@map l as List<CryptoModel>
            }
            .doOnError {
                Timber.e(it, "getMultipleCryptoInformation: error occurred")
            }
    }

    override fun getSpecificCryptoInfoMappedWithFavorites(query: String): Single<CryptoModelDetails> =
        mInstance.getSpecificCryptoCurrencyInformation(query)
                 .map {
                     val index: Int = it.keyList.first()

                     /*this returns CryptoCurrencyDetails*/
                     return@map it.data.get(index)!!
                 }
                 .map{
                     return@map CryptoModelDetails(
                         id = it.id,
                         name = it.name,
                         symbol = it.symbol,
                         category = it.category,
                         description = it.description,
                         slug = it.slug,
                         logo = it.logo,
                         tags = it.tags!!,
                         dateAdded = it.dateAdded)
                 }
                 .doOnError {
                     Timber.e(it, "getSpecificCryptoCurrency: error occurred" )
                 }

    private fun Double?.parseDouble():Double {
        return if(this == null) 0.00
            else{
               String.format("%.2f", this)
                   .toDouble()
           }
    }

    private fun Double?.parseDouble2():Double {
        return if (this == null) 0.00
        else
            DecimalFormat("0.00")
                .format(this)
                .toDouble()
    }

    private fun parseImageString(entityID:Int?):String = String.format(CRYPTO_URL_PATH, entityID)
    private fun parseTagsString(tags: List<String>?):String{
       if(tags == null) return ""

       var s = ""
       for(i in tags.indices){
         s = "$s,$i"
       }

       return s
    }
}