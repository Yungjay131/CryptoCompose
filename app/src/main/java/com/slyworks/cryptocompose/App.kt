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

class App : Application(), ImageLoaderFactory{

    lateinit var appComponent: ApplicationComponent

    companion object{
       /* val imageLoader:ImageLoader = object:ImageLoader{

        }*/
        val imageRequest:ImageRequest.Builder.() -> Unit = {
            memoryCachePolicy(CachePolicy.ENABLED)
            diskCachePolicy(CachePolicy.ENABLED)
            scale(Scale.FILL)
            placeholder(R.drawable.ic_placeholder)
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

}