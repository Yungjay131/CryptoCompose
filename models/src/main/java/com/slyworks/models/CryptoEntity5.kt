package com.slyworks.models


import com.google.gson.annotations.SerializedName

data class CryptoEntity5(
    @SerializedName("data")
    val data: Map<Int,CryptoCurrency5>,
    @SerializedName("status")
    val status: Status
) {
    val keyList:List<Int>
        get() = data.keys.toList()

    data class CryptoCurrency5(
        @SerializedName("category")
        val category: String,
        @SerializedName("contract_address")
        val contractAddress: List<Any>,
        @SerializedName("date_added")
        val dateAdded: String,
        @SerializedName("date_launched")
        val dateLaunched: Any?,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_hidden")
        val isHidden: Int,
        @SerializedName("logo")
        val logo: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("notice")
        val notice: String,
        @SerializedName("platform")
        val platform: Any?,
        @SerializedName("self_reported_circulating_supply")
        val selfReportedCirculatingSupply: Any?,
        @SerializedName("self_reported_market_cap")
        val selfReportedMarketCap: Any?,
        @SerializedName("self_reported_tags")
        val selfReportedTags: Any?,
        @SerializedName("slug")
        val slug: String,
        @SerializedName("subreddit")
        val subreddit: String,
        @SerializedName("symbol")
        val symbol: String,
        @SerializedName("tag-groups")
        val tagGroups: Any?,
        @SerializedName("tag-names")
        val tagNames: Any?,
        @SerializedName("tags")
        val tags: List<String>?,
        @SerializedName("twitter_username")
        val twitterUsername: String,
        @SerializedName("urls")
        val urls: Urls
    ) {
        data class Urls(
            @SerializedName("announcement")
            val announcement: List<Any>,
            @SerializedName("chat")
            val chat: List<Any>,
            @SerializedName("explorer")
            val explorer: List<Any>,
            @SerializedName("facebook")
            val facebook: List<Any>,
            @SerializedName("message_board")
            val messageBoard: List<Any>,
            @SerializedName("reddit")
            val reddit: List<Any>,
            @SerializedName("source_code")
            val sourceCode: List<Any>,
            @SerializedName("technical_doc")
            val technicalDoc: List<Any>,
            @SerializedName("twitter")
            val twitter: List<Any>,
            @SerializedName("website")
            val website: List<Any>
        )
    }

    data class Status(
        @SerializedName("credit_count")
        val creditCount: Int,
        @SerializedName("elapsed")
        val elapsed: Int,
        @SerializedName("error_code")
        val errorCode: Int,
        @SerializedName("error_message")
        val errorMessage: Any?,
        @SerializedName("notice")
        val notice: Any?,
        @SerializedName("timestamp")
        val timestamp: String
    )
}