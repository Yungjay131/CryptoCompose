package com.slyworks.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
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
            val selfReportedCirculatingSupply:Double?,
            val selfReportedMarketCap:Double?,
            val tvlRatio:String?,
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
                data class Quote(val NGN: Ngn):Parcelable{

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
                            val tvl:String?,
                            val lastUpdated: String?) : Parcelable
                }
            }

    override fun toString(): String { return "${data.size} items" }
}

data class CryptoEntity3(@SerializedName("status")
                         var status:Status,
                         @SerializedName("data")
                         var data:List<CryptoCurrency3>){

    data class Status(
        @SerializedName("timestamp")
        var timestamp: String? = null,

        @SerializedName("error_code")
        var errorCode: Int = -1,

        @SerializedName("error_message")
        var errorMessage: String? = null,

        @SerializedName("elapsed")
        var elapsed: Int? = -1,

        @SerializedName("credit_count")
        var creditCount: Int? = -1,

        @SerializedName("notice")
        var notice: String? = null,

        @SerializedName("total_count")
        var totalCount: Int = -1
    )

    data class CryptoCurrency3(
        @SerializedName("id")
        var _id: Int = -1,

        @SerializedName("name")
        var name: String,

        @SerializedName("symbol")
        var symbol: String,

        @SerializedName("date_added")
        var dateAdded: String,

        @SerializedName("max_supply")
        var maxSupply: Double? = -1.0,

        @SerializedName("circulating_supply")
        var circulatingSupply: Double? = -1.0,

        @SerializedName("total_supply")
        var totalSupply: Double? = -1.0,

        @SerializedName("cmc_rank")
        var cmcRank: Int = -1,

        @SerializedName("last_updated")
        var lastUpdated: String,

        @SerializedName("tags")
        var tags: List<String>,

        @SerializedName("quote")
        var quote: Quote
    ){

        data class Quote(
            @SerializedName("NGN")
            var ngn:NGN
        ){
            data class NGN(
                @SerializedName("price")
                var price: Double = -1.0,

                @SerializedName("market_cap")
                var marketCap: Double = -1.0
            )
        }
    }
}


