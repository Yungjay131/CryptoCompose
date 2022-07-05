package com.slyworks.api

import android.util.Log
import com.slyworks.api.CoinMarketApi.Companion.CRYPTO_URL_PATH
import com.slyworks.models.*
import com.slyworks.repository.ApiRepository
import io.reactivex.rxjava3.core.Single
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
        getDataWithFavorites(emptyList())

    override fun getDataWithFavorites(favorites: List<Int>): Single<List<CryptoModel>> {
        mFavoritesList = favorites

        return mInstance.getCryptoInformation()
            .map(::mapResponseToCryptoModel3)
            .doOnError {
                Log.e(TAG, "getData: error occurred", it)
            }
    }

    override fun getMultipleCryptoInformation(ids: String, favoritesList:List<Int>): Single<List<CryptoModel>> {
        mFavoritesList = favoritesList

        return mInstance.getCryptoInformation(ids)
            .map(::mapResponseToCryptoModel2)
            .doOnError {
                Log.e(TAG, "getMultipleCryptoInformation: error occurred", it)
            }
    }

    override fun getSpecificCryptocurrency(query: String): Single<CryptoModelDetails> =
        mInstance.getSpecificCryptoInformation(query)
                 .map{
                     val index:Int = it.keyList.first()
                     val entity:CryptoEntity5.CryptoCurrency5 = it.data.get(index)!!
                     return@map entity
                 }
                 .map(::mapResponseToCryptoModelDetails)
                 .doOnError {
                     Log.e(TAG, "getSpecificCryptoCurrency: error occurred", it )
                 }


    private fun mapResponseToCryptoModelDetails(entity: CryptoEntity5.CryptoCurrency5):CryptoModelDetails {
        return CryptoModelDetails(
            id = entity.id,
            name = entity.name,
            symbol = entity.symbol,
            category = entity.category,
            description = entity.description,
            slug = entity.slug,
            logo = entity.logo,
            tags = entity.tags ?: emptyList(),
            dateAdded = entity.dateAdded )
    }

    private fun mapResponseToCryptoModel3(ce:CryptoEntity3):List<CryptoModel>{
        return with(mutableListOf<CryptoModel>()){
            ce.data.forEach { c:CryptoEntity3.CryptoCurrency3 ->
                add(mapResponseToCryptoModel3Single(c))
            }
            this
        }
    }


    private fun mapResponseToCryptoModel3Single(c:CryptoEntity3.CryptoCurrency3):CryptoModel{
        return CryptoModel(
            _id = c._id,
            image = parseImageString(c._id),
            symbol = c.symbol,
            name = c.name,
            maxSupply = c.maxSupply.parseDouble(),
            circulatingSupply = c.circulatingSupply.parseDouble(),
            totalSupply = c.totalSupply.parseDouble(),
            cmcRank = c.cmcRank,
            lastUpdated = c.lastUpdated,
            price = c.quote.ngn.price,
            priceUnit = "₦",
            marketCap = c.quote.ngn.marketCap.parseDouble(),
            dateAdded = c.dateAdded,
            tags = parseTagsString(c.tags),
            isFavorite = mFavoritesList.contains(c._id))
    }

 private fun mapResponseToCryptoModel2(ce:CryptoEntity4):List<CryptoModel>{
        return with(mutableListOf<CryptoModel>()){
            ce.data.forEach { c ->
                add(mapResponseToCryptoModel2Single(c.value))
            }
            this
        }
    }

    private fun mapResponseToCryptoModel2Single(c:CryptoEntity4.CryptoCurrency4):CryptoModel{
        return CryptoModel(
            _id = c.id,
            image = parseImageString(c.id),
            symbol = c.symbol,
            name = c.name,
            maxSupply = c.maxSupply.parseDouble(),
            circulatingSupply = c.circulatingSupply.parseDouble(),
            totalSupply = c.totalSupply.parseDouble(),
            cmcRank = c.cmcRank,
            lastUpdated = c.lastUpdated,
            price = c.quote.nGN.price,
            priceUnit = "₦",
            marketCap = c.quote.nGN.marketCap.parseDouble(),
            dateAdded = c.dateAdded,
            tags = parseTagsString(c.tags),
            isFavorite = mFavoritesList.contains(c.id))
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