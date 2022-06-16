package com.slyworks.cryptocompose.ui.activities.main

import com.slyworks.models.CryptoModel


/**
 *Created by Joshua Sylvanus, 4:08 AM, 16-Jun-22.
 */
interface IMainActivityViewModel {
    fun setItemFavoriteStatus(entity:CryptoModel,
                              status:Boolean)
}