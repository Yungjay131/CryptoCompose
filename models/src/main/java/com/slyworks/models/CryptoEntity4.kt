package com.slyworks.models


import com.google.gson.annotations.SerializedName

data class CryptoEntity4(
    @SerializedName("status")
    val status: Status,
    @SerializedName("data")
    val data: Map<Int,CryptoCurrency4>
) {
    val keyList:List<Int>
    get() = data.keys.toList()

    data class Status(
        @SerializedName("credit_count")
        val creditCount: Int,
        @SerializedName("elapsed")
        val elapsed: Int,
        @SerializedName("error_code")
        val errorCode: Int,
        @SerializedName("error_message")
        val errorMessage: String?,
        @SerializedName("notice")
        val notice: String?,
        @SerializedName("timestamp")
        val timestamp: String
    )

    data class CryptoCurrency4(
        @SerializedName("circulating_supply")
        val circulatingSupply: Double,
        @SerializedName("cmc_rank")
        val cmcRank: Int,
        @SerializedName("date_added")
        val dateAdded: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_active")
        val isActive: Int,
        @SerializedName("is_fiat")
        val isFiat: Int,
        @SerializedName("last_updated")
        val lastUpdated: String,
        @SerializedName("max_supply")
        val maxSupply: Double,
        @SerializedName("name")
        val name: String,
        @SerializedName("num_market_pairs")
        val numMarketPairs: Int,
        @SerializedName("platform")
        val platform: Any?,
        @SerializedName("quote")
        val quote: Quote,
        @SerializedName("self_reported_circulating_supply")
        val selfReportedCirculatingSupply: Any?,
        @SerializedName("self_reported_market_cap")
        val selfReportedMarketCap: Any?,
        @SerializedName("slug")
        val slug: String,
        @SerializedName("symbol")
        val symbol: String,
        @SerializedName("tags")
        val tags: List<String>,
        @SerializedName("total_supply")
        val totalSupply: Double,
        @SerializedName("tvl_ratio")
        val tvlRatio: Any?
    ) {
        data class Quote(
            @SerializedName("NGN")
            val nGN: NGN
        ) {
            data class NGN(
                @SerializedName("fully_diluted_market_cap")
                val fullyDilutedMarketCap: Double,
                @SerializedName("last_updated")
                val lastUpdated: String,
                @SerializedName("market_cap")
                val marketCap: Double,
                @SerializedName("market_cap_dominance")
                val marketCapDominance: Double,
                @SerializedName("percent_change_1h")
                val percentChange1h: Double,
                @SerializedName("percent_change_24h")
                val percentChange24h: Double,
                @SerializedName("percent_change_30d")
                val percentChange30d: Double,
                @SerializedName("percent_change_60d")
                val percentChange60d: Double,
                @SerializedName("percent_change_7d")
                val percentChange7d: Double,
                @SerializedName("percent_change_90d")
                val percentChange90d: Double,
                @SerializedName("price")
                val price: Double,
                @SerializedName("tvl")
                val tvl: Any?,
                @SerializedName("volume_24h")
                val volume24h: Double,
                @SerializedName("volume_change_24h")
                val volumeChange24h: Double
            )
        }
    }


}