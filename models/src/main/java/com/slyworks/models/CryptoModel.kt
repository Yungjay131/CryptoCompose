package com.slyworks.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CryptoModel(
    val _id: Int,
    val image:String,
    val symbol:String,
    val name:String,
    val maxSupply:String?,
    val circulatingSupply:String?,
    val totalSupply:String?,
    val cmcRank:Int,
    val lastUpdated:String,
    val price:String,
    val priceUnit:String,
    val marketCap:String?,
    val dateAdded: String,
    val tags:String,//this would be a , separated string
    var isFavorite:Boolean = false
) : Parcelable