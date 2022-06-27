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
    var maxSupply:Double = 0.0,
    var circulatingSupply:Double = 0.0,
    var totalSupply:Double = 0.0,
    var cmcRank:Int = 0,
    @Required
    var lastUpdated:String = "",
    var price:Double = 0.0,
    @Required
    var priceUnit:String = "",
    var marketCap:Double = 0.0,
    @Required
    var dateAdded: String = "",
    @Required
    var tags:String = "",
    var isFavorite:Boolean = false
): RealmObject()
