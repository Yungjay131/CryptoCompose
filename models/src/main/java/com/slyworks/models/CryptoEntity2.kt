package com.slyworks.models

import com.google.gson.annotations.SerializedName


/**
 *Created by Joshua Sylvanus, 7:48 PM, 18/06/2022.
 */
data class CryptoEntity2(@SerializedName("status")
                         val status:Status,
                         @SerializedName("data")
                         val data:CryptoCurrency2) {

    data class Status( @SerializedName("timestamp")
                       val timeStamp: String,
                       @SerializedName("error_code")
                      val errorCode: Int,
                       @SerializedName("error_message")
                      val errorMessage: String?,
                       @SerializedName("elapsed")
                      val elapsed: Int,
                       @SerializedName("credit_count")
                      val creditCount: Int,
                       @SerializedName("notice")
                      val notice: String? )

    data class CryptoCurrency2(@SerializedName("1")
                               val one:One){


        data class One( @SerializedName("id")
                       val id:Int,
                        @SerializedName("name")
                        val name:String,
                        @SerializedName("symbol")
                        val symbol:String,
                        @SerializedName("category")
                       val category:String,
                        @SerializedName("description")
                       val description:String,
                        @SerializedName("slug")
                       val slug:String,
                        @SerializedName("logo")
                       val logo:String,
                       val subreddit:String,
                       val notice:String?,
                        @SerializedName("tags")
                       val tags:List<String>,
                       val tagNames:List<String>,
                       val tagGroups:List<String>,
                       val urls:Urls,
                       val platform:String?,
                        @SerializedName("date_added")
                       val dateAdded:String,
                       val twitterUsername:String,
                       val isHidden:Int,
                       val dateLaunched:String?,
                       val contactAddress:List<String>,
                       val selfReportedCirculatingSupply:Double?,
                       val selfReportedTags:String?,
                       val selfReportedMarketCap:Double?){

            data class Urls(val website:List<String>,
                            val twitter:List<String>,
                            val messageBoard:List<String>,
                            val chat:List<String>,
                            val facebook:List<String>,
                            val explorer:List<String>,
                            val reddit:List<String>,
                            val technicalDoc:List<String>,
                            val sourceCode:List<String>,
                            val announcement:List<String>)
        }
    }
}

