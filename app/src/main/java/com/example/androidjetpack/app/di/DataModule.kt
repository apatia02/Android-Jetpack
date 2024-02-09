package com.example.androidjetpack.app.di

import com.example.androidjetpack.data.retrofit.BASE_URL
import com.example.androidjetpack.data.retrofit.MovieApi
import com.example.androidjetpack.data.repository.MovieRepositoryImpl
import com.example.androidjetpack.data.retrofit.KeyInterceptor
import com.example.androidjetpack.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Модуль для дата слоя
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideKeyInterceptor(): KeyInterceptor {
        return KeyInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(keyInterceptor: KeyInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(keyInterceptor)
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

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(movieApi: MovieApi): MovieRepository {
        return MovieRepositoryImpl(movieApi)
    }
}