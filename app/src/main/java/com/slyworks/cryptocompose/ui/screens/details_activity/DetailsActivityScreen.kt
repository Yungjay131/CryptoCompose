package com.slyworks.cryptocompose.ui.screens.details_activity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.slyworks.cryptocompose.R

sealed class DetailsActivityScreen(val route:String,
                                   @StringRes val label:Int,
                                   @DrawableRes val icon: Int){
    companion object{
        const val route_details = "Details"
    }

    private object DetailsScreen: DetailsActivityScreen(
        route_details,
        R.string.details_screen,
        R.drawable.ic_list)
}