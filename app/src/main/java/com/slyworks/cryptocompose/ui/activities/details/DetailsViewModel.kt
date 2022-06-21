package com.slyworks.cryptocompose.ui.activities.details

import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.data.DataManager
import com.slyworks.models.CryptoModel


/**
 *Created by Joshua Sylvanus, 5:31 PM, 17-Jun-22.
 */
class DetailsViewModel(dataManager: DataManager) : ViewModel() , IViewModel {
    override fun setItemFavoriteStatus(entity: CryptoModel, status: Boolean) {

    }
}