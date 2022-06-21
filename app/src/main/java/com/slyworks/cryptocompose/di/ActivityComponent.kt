package com.slyworks.cryptocompose.di

import androidx.activity.ComponentActivity
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.slyworks.cryptocompose.ui.activities.details.DetailsActivity
import com.slyworks.cryptocompose.ui.activities.main.MainActivity
import com.slyworks.di.ActivityScope
import dagger.BindsInstance
import dagger.Subcomponent


/**
 *Created by Joshua Sylvanus, 9:37 AM, 05-Jun-22.
 */
@Subcomponent(
    modules = [
        ActivityModule::class,
    ]
)
@ActivityScope
interface ActivityComponent {
    @ExperimentalUnitApi
    fun inject(activity: MainActivity)
    fun inject(activity: DetailsActivity)

    @Subcomponent.Builder
    interface Builder{
        fun componentActivity(@BindsInstance activity: ComponentActivity):Builder
        fun build():ActivityComponent
    }
}