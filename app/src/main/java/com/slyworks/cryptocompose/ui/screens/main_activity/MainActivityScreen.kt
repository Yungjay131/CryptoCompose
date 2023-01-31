package com.slyworks.cryptocompose.ui.screens.main_activity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.slyworks.cryptocompose.R


/**
 *Created by Joshua Sylvanus, 4:57 AM, 11-Jun-22.
 */
sealed class MainActivityScreen(val route:String,
                                @StringRes val label:Int,
                                @DrawableRes val icon:Int){

    companion object{
        const val route_home = "Home"
        const val route_search = "Search"
        const val route_favorites = "Favorites"

        val screensList:List<MainActivityScreen> = listOf(
            HomeScreen,
            SearchScreen,
            FavoritesScreen
        )
    }

    private object HomeScreen: MainActivityScreen(
        route_home,
        R.string.home_screen,
        R.drawable.ic_home )
    private object SearchScreen: MainActivityScreen(
        route_search,
        R.string.search_screen,
        R.drawable.ic_search )
    private object FavoritesScreen: MainActivityScreen(
        route_favorites,
        R.string.favorite_screen,
        R.drawable.ic_favorite )
}

