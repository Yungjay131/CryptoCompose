package com.slyworks.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 *Created by Joshua Sylvanus, 10:27 PM, 8/19/2021.
 */
@Parcelize
data class CryptoEntity(
        val status: Status,
        val data: List<CryptoCurrency>):Parcelable{

    @Parcelize
    data class Status(
            val timestamp: String?,
            val errorCode: Int,
            val errorMessage: String?,
            val elapsed: Int?,
            val creditCount: Int?,
            val notice: String?,
            val totalCount: Int):Parcelable

    @Parcelize
    data class CryptoCurrency(
            val id: Int,
            val name: String?,
            val symbol: String?,
            val slug: String?,
            val numMarketPairs: Int,
            val dateAdded: String?,
            val tags: List<String>?,
            val maxSupply: Double?,
            val circulatingSupply: Double?,
            val totalSupply: Double?,
            val platform: Platform?,
            val cmcRank: Int,
            val lastUpdated: String?,
            val quote: Quote? = null):Parcelable{

                @Parcelize
                data class Platform(
                    var id:Int? = null,
                    var name:String? = null,
                    var symbol:String? = null,
                    var slug:String? = null,
                    var tokenAddress:String? = null):Parcelable

                @Parcelize
                data class Quote(val ngn: Ngn):Parcelable{

                    @Parcelize
                    data class Ngn(
                            val price: Double,
                            val volume24h: Double,
                            val percentChange1h: Double,
                            val percentChange24h: Double,
                            val percentChange7d: Double,
                            val percentChange30d: Double,
                            val percentChange60d: Double,
                            val percentChange90d: Double,
                            val marketCap: Double,
                            val marketCapDominance: Double,
                            val fullyDilutedMarketCap: Double,
                            val lastUpdated: String?) : Parcelable
                }
            }

    override fun toString(): String { return "${data.size} items" }
}


