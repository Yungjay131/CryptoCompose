package com.slyworks.cryptocompose

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.facebook.stetho.Stetho
import com.slyworks.cryptocompose.di.ApplicationComponent
import com.slyworks.cryptocompose.di.DaggerApplicationComponent
import com.slyworks.models.Conf
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 9:40 AM, 05-Jun-22.
 */

val Context.appComp: ApplicationComponent
get() = (applicationContext as App).appComponent

class App : Application(), ImageLoaderFactory{

    lateinit var appComponent: ApplicationComponent

    companion object{
        val imageRequest:ImageRequest.Builder.() -> Unit = {
            memoryCachePolicy(CachePolicy.ENABLED)
            diskCachePolicy(CachePolicy.ENABLED)
            scale(Scale.FIT)
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_placeholder)
            transformations(CircleCropTransformation())
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        val config:RealmConfiguration = RealmConfiguration.Builder()
            .schemaVersion(1L)
            .build()

        Stetho.initializeWithDefaults(this)

        appComponent = DaggerApplicationComponent
            .builder()
            .componentConf(Conf)
            .componentRealmConfig(config)
            .componentApplication(this)
            .componentContext(this)
            .build()
            .also {
              it.inject(this@App)
            }


        /*init Timber logger, its an api dependency in :repository*/
        Timber.plant(object: Timber.DebugTree(){
            override fun createStackElementTag(element: StackTraceElement): String {
                return String.format(
                    "%s:%s",
                    super.createStackElementTag(element),
                    element.methodName)
            }
        })
        Timber.e("app created")
    }

}