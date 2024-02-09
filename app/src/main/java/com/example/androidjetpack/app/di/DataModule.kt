package com.example.androidjetpack.app.di

import android.content.Context
import androidx.room.Room
import com.example.androidjetpack.data.repository.FavoriteMoviesRepositoryImpl
import com.example.androidjetpack.data.repository.MovieRepositoryImpl
import com.example.androidjetpack.data.retrofit.BASE_URL
import com.example.androidjetpack.data.retrofit.KeyInterceptor
import com.example.androidjetpack.data.retrofit.MovieApi
import com.example.androidjetpack.data.room.AppDatabase
import com.example.androidjetpack.data.room.FavoriteMoviesDao
import com.example.androidjetpack.data.room.NAME_DB
import com.example.androidjetpack.domain.repository.FavoriteMoviesRepository
import com.example.androidjetpack.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    //region Room FavoriteMovies
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            NAME_DB
        ).build()
    }

    @Provides
    fun provideFavoriteMoviesDao(database: AppDatabase): FavoriteMoviesDao {
        return database.favoriteMoviesDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteMoviesRepository(favoriteMoviesDao: FavoriteMoviesDao): FavoriteMoviesRepository {
        return FavoriteMoviesRepositoryImpl(favoriteMoviesDao)
    }
    //endregion

    //region Retrofit Movies
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
    //endregion
}