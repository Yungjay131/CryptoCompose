package app.slyworks.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *Created by Joshua Sylvanus, 4:16 AM, 11-Jun-22.
 */

@Entity
data class CryptoModelIDRoom(
    @PrimaryKey
    @ColumnInfo(name = "id")var id:Int)