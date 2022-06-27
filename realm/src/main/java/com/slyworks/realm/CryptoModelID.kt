package com.slyworks.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

/**
 *Created by Joshua Sylvanus, 4:16 AM, 11-Jun-22.
 */
open class CryptoModelID(
    @PrimaryKey
    var id:Int = 0
): RealmObject()