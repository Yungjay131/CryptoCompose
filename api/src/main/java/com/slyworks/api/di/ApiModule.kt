package com.slyworks.api.di

import android.app.Application
import com.google.gson.GsonBuilder
import com.slyworks.api.ApiRepositoryImpl
import com.slyworks.api.CoinMarketApi
import com.slyworks.di.ApplicationScope
import com.slyworks.models.NetworkConf
import com.slyworks.repository.ApiRepository
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


/**
 *Created by Joshua Sylvanus, 3:33 PM, 04-Jun-22.
 */

@Module
object ApiModule {
    @Provides
    @ApplicationScope
    fun provideCache(conf: NetworkConf,
                     application: Application
    ): Cache =
        Cache(application.cacheDir,
              conf.cacheSize )

    @Provides
    @ApplicationScope
    fun provideHttpClient(cache: Cache): OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .build()

    @Provides
    @ApplicationScope
    fun provideRetrofit(conf: NetworkConf,
                        httpClient: OkHttpClient):Retrofit =
        Retrofit.Builder()
            .baseUrl(conf.baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat(conf.dateFormat)
                        .create()
                )
            )
            .client(httpClient)
            .build()

    @Provides
    @ApplicationScope
    fun provideCryptoEndpoint(retrofit: Retrofit): CoinMarketApi {
        return retrofit.create(CoinMarketApi::class.java)
    }

    @Provides
    @ApplicationScope
    fun provideApiRepository(api:CoinMarketApi): ApiRepository {
        return ApiRepositoryImpl(api)
    }
}