package com.slyworks.cryptocompose.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.slyworks.cryptocompose.MViewModelFactory
import com.slyworks.cryptocompose.ui.activities.details.DetailsViewModel
import com.slyworks.cryptocompose.ui.activities.main.FavoritesViewModel
import com.slyworks.cryptocompose.ui.activities.main.HomeViewModel
import com.slyworks.cryptocompose.ui.activities.main.SearchViewModel
import com.slyworks.data.DataManager
import com.slyworks.di.ActivityScope
import dagger.Module
import dagger.Provides


/**
 *Created by Joshua Sylvanus, 9:34 AM, 05-Jun-22.
 */

@Module
class ActivityModule {
    @Provides
    @ActivityScope
    fun provideHomeViewModel(activity: ComponentActivity,
                                     dataManager: DataManager): HomeViewModel {
        return ViewModelProvider(activity.viewModelStore,
            MViewModelFactory(dataManager))
            .get(HomeViewModel::class.java)
    }

    @Provides
    @ActivityScope
    fun provideSearchViewModel(activity: ComponentActivity,
                                     dataManager: DataManager): SearchViewModel {
        return ViewModelProvider(activity.viewModelStore,
            MViewModelFactory(dataManager))
            .get(SearchViewModel::class.java)
    }

    @Provides
    @ActivityScope
    fun provideFavoritesViewModel(activity: ComponentActivity,
                                     dataManager: DataManager): FavoritesViewModel {
        return ViewModelProvider(activity.viewModelStore,
            MViewModelFactory(dataManager))
            .get(FavoritesViewModel::class.java)
    }

    @Provides
    @ActivityScope
    fun provideDetailsViewModel(activity: ComponentActivity,
                                dataManager: DataManager): DetailsViewModel {
        return ViewModelProvider(activity.viewModelStore,
            MViewModelFactory(dataManager))
            .get(DetailsViewModel::class.java)
    }
}

