package com.slyworks.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId


open class CryptoModelRealm(
    @PrimaryKey
    //var id:String = ObjectId().toHexString(),
    var _id:Int = 0,
    @Required
    var image:String = "",
    @Required
    var symbol:String = "",
    @Required
    var name:String = "",
    var maxSupply:String = "0.0",
    var circulatingSupply:String = "0.0",
    var totalSupply:String = "0.0",
    var cmcRank:Int = 0,
    @Required
    var lastUpdated:String = "",
    var price:String = "0.0",
    @Required
    var priceUnit:String = "",
    var marketCap:String = "0.0",
    @Required
    var dateAdded: String = "",
    @Required
    var tags:String = "",
    var isFavorite:Boolean = false
): RealmObject()
