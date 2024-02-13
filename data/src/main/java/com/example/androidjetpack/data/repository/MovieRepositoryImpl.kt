package com.example.androidjetpack.data.repository

import com.example.androidjetpack.data.mappers.transformInMovieList
import com.example.androidjetpack.data.movie.MovieApi
import com.example.androidjetpack.domain.entity.MovieList
import com.example.androidjetpack.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * Репозиторий для получения фильмов
 */
internal class MovieRepositoryImpl @Inject constructor(private val movieApi: MovieApi) :
    MovieRepository {

    /**
     * Запрос на получение фильмов
     */
    override suspend fun getMovies(): MovieList {
        return movieApi.getMovies().transformInMovieList()
    }

    /**
     * Запрос на получение фильтрованного списка фильмов
     */
    override suspend fun searchMovies(query: String): MovieList {
        return movieApi.searchMovies(query).transformInMovieList()
    }
}