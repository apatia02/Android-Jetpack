package com.example.androidjetpack.data.di

import com.example.androidjetpack.data.movie.MovieApi
import com.example.androidjetpack.data.repository.FavoriteMoviesRepositoryImpl
import com.example.androidjetpack.data.repository.MovieRepositoryImpl
import com.example.androidjetpack.data.room.AppDatabase
import com.example.androidjetpack.data.movie.FavoriteMoviesDao
import com.example.androidjetpack.domain.repository.FavoriteMoviesRepository
import com.example.androidjetpack.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MoviesDiModule {

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

    @Provides
    fun provideFavoriteMoviesDao(database: AppDatabase): FavoriteMoviesDao {
        return database.favoriteMoviesDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteMoviesRepository(favoriteMoviesDao: FavoriteMoviesDao): FavoriteMoviesRepository {
        return FavoriteMoviesRepositoryImpl(favoriteMoviesDao)
    }
}
