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
class ApiRepositoryImpl(private val mInstance:CoinMarketApi): ApiRepository {
    //region Vars
    private val TAG: String? = ApiRepositoryImpl::class.simpleName

    private lateinit var mFavoritesList:List<Int>
    //endregion

    override fun getData(favoriteIDs:List<Int>):Single<List<CryptoModel>> =
        getAllCryptoInfoMappedWithFavorites(emptyList())

    override fun getAllCryptoInfoMappedWithFavorites(favorites: List<Int>): Single<List<CryptoModel>> {
        mFavoritesList = favorites

        return mInstance.getAllCryptoCurrencyInfo()
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
                        price = e.quote.ngn.price.parseDouble(),
                        priceUnit = "₦",
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

        return mInstance.getMultipleCryptoCurrencyInfo(ids)
            .map{
                val l = mutableListOf<CryptoModel>()

                it.keyList.forEach { i ->
                    val e:CryptoEntityMultiple.CryptoCurrencyMultiple = it.data[i]!!
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
                        price = e.quote.nGN.price.parseDouble(),
                        priceUnit = "₦",
                        marketCap = e.quote.nGN.marketCap.parseDouble(),
                        dateAdded = e.dateAdded,
                        tags = parseTagsString(e.tags),
                        isFavorite = mFavoritesList.contains(e.id))

                    l.add(model)
                }

                return@map l as List<CryptoModel>
            }
            .doOnError {
                Timber.e(it, "getMultipleCryptoInformation: error occurred")
            }
    }

    override fun getSpecificCryptoInfoForID(query: String): Single<CryptoModelDetails> =
        mInstance.getSpecificCryptoCurrencyInfoForID(query)
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
                    tags = it.tags ?: emptyList(),
                    dateAdded = it.dateAdded)
            }
            .doOnError {
                Timber.e(it, "getSpecificCryptoCurrency: error occurred" )
            }


    override fun getSpecificCryptoInfo(query: String): Single<CryptoModelDetails> =
        mInstance.getSpecificCryptoCurrencyInfo(query)
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
                         tags = it.tags ?: emptyList(),
                         dateAdded = it.dateAdded)
                 }
                 .doOnError {
                     Timber.e(it, "getSpecificCryptoCurrency: error occurred" )
                 }

    private fun Double?.parseDouble2():Double {
        return if(this == null) 0.00
            else{
               String.format("%.2f", this)
                   .toDouble()
           }
    }

    private fun Double?.parseDouble():String {
        return if (this == null) "0.00"
               else
                   DecimalFormat("#,###.##")
                       .format(this)
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