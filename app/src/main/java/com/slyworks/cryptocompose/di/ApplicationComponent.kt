package com.slyworks.cryptocompose.di

import android.app.Application
import android.content.Context
import com.slyworks.cryptocompose.App
import com.slyworks.di.ApplicationScope
import com.slyworks.models.NetworkConf
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import io.realm.RealmConfiguration


/**
 *Created by Joshua Sylvanus, 4:04 PM, 04-Jun-22.
 */

@Component(
    /*a list of types used as components dependencies*/
    //dependencies = [ NetworkConf::class ],
    modules = [
        ApplicationModule::class,
    ]
)
@ApplicationScope
interface ApplicationComponent {
    fun inject(app:App)

    fun activityComponentBuilder(): ActivityComponent.Builder

    @Component.Builder
    interface Builder {
        fun componentRealmConfig(@BindsInstance config:RealmConfiguration):Builder
        fun componentContext(@BindsInstance context: Context):Builder
        fun componentApplication(@BindsInstance app:Application): Builder
        fun componentConf(@BindsInstance conf:NetworkConf):Builder
        fun build():ApplicationComponent
    }
}