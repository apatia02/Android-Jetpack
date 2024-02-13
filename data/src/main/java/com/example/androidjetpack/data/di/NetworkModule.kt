package com.example.androidjetpack.data.di

import android.util.Log
import com.example.androidjetpack.data.BuildConfig
import com.example.androidjetpack.data.network.KeyInterceptor
import com.example.androidjetpack.data.network.ServerConstants.BASE_URL
import com.example.androidjetpack.data.network.ServerConstants.HTTP_LOG_TAG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Модуль для нетворк
 */
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = Logger { message -> Log.d(HTTP_LOG_TAG, message) }
        return HttpLoggingInterceptor(logger).apply {
            level = when (BuildConfig.DEBUG) {
                true -> Level.BODY
                false -> Level.BASIC
            }
        }
    }

    @Provides
    @Singleton
    fun provideKeyInterceptor(): KeyInterceptor {
        return KeyInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        keyInterceptor: KeyInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(keyInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}