package com.example.androidjetpack.data.repository

import com.example.androidjetpack.data.mappers.transformInGenreList
import com.example.androidjetpack.data.mappers.transformInMovieList
import com.example.androidjetpack.data.movie.MovieApi
import com.example.androidjetpack.domain.entity.GenreList
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
    override suspend fun getMovies(page:Int): MovieList {
        return movieApi.getMovies(page).transformInMovieList()
    }

    /**
     * Запрос на получение фильтрованного списка фильмов
     */
    override suspend fun searchMovies(query: String, page:Int): MovieList {
        return movieApi.searchMovies(query, page).transformInMovieList()
    }

    /**
     * Запрос на получение списка жанров
     */
    override suspend fun getGenres(): GenreList {
        return  movieApi.getGenres().transformInGenreList()
    }
}