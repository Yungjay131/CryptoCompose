package com.slyworks.cryptocompose

import com.slyworks.models.CryptoModel


/**
 *Created by Joshua Sylvanus, 4:08 AM, 16-Jun-22.
 */
interface IViewModel {
    fun setItemFavoriteStatus(entity:CryptoModel,
                              status:Boolean)
}