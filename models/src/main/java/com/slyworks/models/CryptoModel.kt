package com.slyworks.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CryptoModel(
    val _id: Int,
    val image:String,
    val symbol:String,
    val name:String,
    val maxSupply:Double?,
    val circulatingSupply:Double?,
    val totalSupply:Double?,
    val cmcRank:Int,
    val lastUpdated:String,
    val price:Double,
    val priceUnit:String,
    val marketCap:Double?,
    val dateAdded: String,
    val tags:String,//this would be a , separated string
    val isFavorite:Boolean = false
) : Parcelable