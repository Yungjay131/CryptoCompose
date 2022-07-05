package com.slyworks.models

import com.google.gson.annotations.SerializedName

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
            var ngn:Currency
        ){
            data class Currency(
                @SerializedName("price")
                var price: Double = -1.0,

                @SerializedName("market_cap")
                var marketCap: Double = -1.0
            )
        }
    }
}