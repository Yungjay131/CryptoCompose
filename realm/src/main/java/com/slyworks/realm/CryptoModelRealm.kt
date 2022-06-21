package com.slyworks.realm

import com.slyworks.models.CryptoModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId


/**
 *Created by Joshua Sylvanus, 4:16 AM, 11-Jun-22.
 */
open class CryptoModelID(
    @PrimaryKey
    var id:String = ObjectId().toHexString(),
    var _id:Int = 0
):RealmObject()

open class CryptoModelRealm(
    @PrimaryKey
    var id:String = ObjectId().toHexString(),
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
): RealmObject(){

    companion object{
        fun fromCryptoModel(model: CryptoModel): CryptoModelRealm {
            return CryptoModelRealm(
                id = model.id,
                _id = model._id,
                image = model.image,
                symbol = model.symbol,
                name = model.name,
                maxSupply = model.maxSupply ?: 0.0,
                circulatingSupply = model.circulatingSupply ?: 0.0,
                totalSupply = model.totalSupply ?: 0.0,
                cmcRank = model.cmcRank,
                lastUpdated = model.lastUpdated,
                price = model.price,
                marketCap = model.marketCap ?: 0.0,
                dateAdded = model.dateAdded,
                tags = model.tags,
                isFavorite = model.isFavorite )
        }
    }
}
