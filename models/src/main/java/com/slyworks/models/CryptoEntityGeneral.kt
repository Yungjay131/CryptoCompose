package com.slyworks.models

import com.google.gson.annotations.SerializedName

data class CryptoEntityGeneral(
    @SerializedName("status")
                         var status:Status,
                         @SerializedName("data")
                         var data:List<CryptoCurrencyGeneral>){

    data class Status(
        @SerializedName("timestamp")
        val timestamp: String?,
        @SerializedName("error_code")
        val errorCode: Int,
        @SerializedName("error_message")
        val errorMessage: String?,
        @SerializedName("elapsed")
        val elapsed: Int?,
        @SerializedName("credit_count")
        val creditCount: Int?,
        @SerializedName("notice")
        val notice: String?,
        @SerializedName("total_count")
        val totalCount: Int
    )

    data class CryptoCurrencyGeneral(
        @SerializedName("id")
        val _id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("symbol")
        val symbol: String,
        @SerializedName("date_added")
        val dateAdded: String,
        @SerializedName("max_supply")
        val maxSupply: Double?,
        @SerializedName("circulating_supply")
        val circulatingSupply: Double?,
        @SerializedName("total_supply")
        val totalSupply: Double?,
        @SerializedName("cmc_rank")
        val cmcRank: Int,
        @SerializedName("last_updated")
        val lastUpdated: String,
        @SerializedName("tags")
        val tags: List<String>,
        @SerializedName("quote")
        val quote: Quote
    ){

        data class Quote(
            @SerializedName("NGN")
            val ngn:Currency
        ){
            data class Currency(
                @SerializedName("price")
                val price: Double,
                @SerializedName("market_cap")
                val marketCap: Double
            )
        }
    }
}