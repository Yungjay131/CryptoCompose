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
    //endregion

    override fun getData():Single<List<CryptoModel>> =
        mInstance.getCryptoInformation()
                 .map(::mapResponseToCryptoModel2)
                 .doOnError {
                     Log.e(TAG, "getData: error occurred", it )
                 }

    override fun getSpecificCryptocurrency(query: String): Single<CryptoModelDetails> =
        mInstance.getSpecificCryptoInformation(query)
                 .map(::mapResponseToCryptoModelDetails)
                 .doOnError {
                     Log.e(TAG, "getSpecificCryptoCurrency: error occurred", it )
                 }


    private fun mapResponseToCryptoModelDetails(entity:CryptoEntity2):CryptoModelDetails {
        return CryptoModelDetails(
            id = entity.data.one.id,
            name = entity.data.one.name,
            symbol = entity.data.one.symbol,
            category = entity.data.one.category,
            description = entity.data.one.description,
            slug = entity.data.one.slug,
            logo = entity.data.one.logo,
            tags = entity.data.one.tags,
            dateAdded = entity.data.one.dateAdded )
    }

    private fun mapResponseToCryptoModel(c:CryptoEntity):List<CryptoModel>{
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
                                price = it.quote?.NGN?.price ?: 0.0,
                                priceUnit = "₦",
                                marketCap = it.quote?.NGN?.marketCap,
                                dateAdded = it.dateAdded ?: "",
                                tags = parseTagsString(it.tags)
                                )

            l.add(e)
        }

        return l
    }

    private fun mapResponseToCryptoModel2(ce:CryptoEntity3):List<CryptoModel>{
        return with(mutableListOf<CryptoModel>()){
            ce.data.forEach { c ->
                add(
                    CryptoModel(
                        id = "",
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
                        tags = parseTagsString(c.tags) )
                )
            }
            this
        }
    }

    private fun Double?.parseDouble():Double =
          if(this == null) 0.00
          else
              DecimalFormat("0.00")
                  .format(this)
                  .toDouble()


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