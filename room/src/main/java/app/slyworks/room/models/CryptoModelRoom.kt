package app.slyworks.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.util.TableInfo

@Entity(indices = [Index(value = ["id"]), Index(value = ["is_favorite"])] )
data class CryptoModelRoom(
    @PrimaryKey
    @ColumnInfo(name = "id") val _id:Int,
    @ColumnInfo(name = "image") val image:String,
    @ColumnInfo(name = "symbol") val symbol:String,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "max_supply") val maxSupply:String,
    @ColumnInfo(name = "circulating_supply") val circulatingSupply:String,
    @ColumnInfo(name = "total_supply") val totalSupply:String,
    @ColumnInfo(name = "cmc_rank") val cmcRank:Int,
    @ColumnInfo(name = "last_updated") val lastUpdated:String,
    @ColumnInfo(name = "price") val price:String,
    @ColumnInfo(name = "price_unit") val priceUnit:String,
    @ColumnInfo(name = "market_cap") val marketCap:String,
    @ColumnInfo(name = "date_added") val dateAdded: String,
    @ColumnInfo(name = "tags") val tags:String,
    @ColumnInfo(name = "is_favorite") var isFavorite:Boolean = false )
