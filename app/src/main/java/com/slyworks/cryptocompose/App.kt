package com.slyworks.cryptocompose

import android.app.Application
import android.content.Context
import com.slyworks.cryptocompose.di.ApplicationComponent
import com.slyworks.cryptocompose.di.DaggerApplicationComponent
import com.slyworks.models.Conf
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 *Created by Joshua Sylvanus, 9:40 AM, 05-Jun-22.
 */

val Context.appComp: ApplicationComponent
get() = (applicationContext as App).appComponent

class App : Application() {

    lateinit var appComponent: ApplicationComponent


    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val config:RealmConfiguration = RealmConfiguration.Builder()
            .schemaVersion(1L)
            .build()

        appComponent = DaggerApplicationComponent
            .builder()
            .componentConf(Conf)
            .componentRealmConfig(config)
            .componentApplication(this)
            .componentContext(this)
            .build()
            .apply {
              inject(this@App)
            }

    }

    private fun initRealm(){

    }
}