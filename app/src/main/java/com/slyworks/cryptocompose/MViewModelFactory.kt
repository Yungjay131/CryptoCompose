package com.slyworks.cryptocompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slyworks.cryptocompose.ui.activities.main.FavoritesViewModel
import com.slyworks.cryptocompose.ui.activities.main.HomeViewModel
import com.slyworks.cryptocompose.ui.activities.main.SearchViewModel
import com.slyworks.data.DataManager

class MViewModelFactory(private var dataManager: DataManager):
    ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(dataManager) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(dataManager) as T
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) ->
                FavoritesViewModel(dataManager) as T
            else -> throw IllegalArgumentException("setup ViewModel Factory before instantiating")
        }
    }
}