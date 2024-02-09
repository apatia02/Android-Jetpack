package com.example.androidjetpack.domain.use_case

import com.example.androidjetpack.domain.entity.MovieList
import com.example.androidjetpack.domain.repository.FavoriteMoviesRepository
import com.example.androidjetpack.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * UseCase, который проставляет к спискам фильмам, являются ли они избранными.
 */
class MoviesUseCase @Inject constructor(
    private val moviesRepository: MovieRepository,
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {
    /**
     * Запрос на получение фильмов
     */
    suspend fun getMovies(): MovieList = moviesRepository.getMovies().setFavoriteStatusForMovies()


    /**
     * Запрос на получение фильтрованного списка фильмов
     */
    suspend fun searchMovies(query: String): MovieList =
        moviesRepository.searchMovies(query).setFavoriteStatusForMovies()

    /**
     * Установка у фильмов статуса избранности
     */
    private suspend fun MovieList.setFavoriteStatusForMovies(): MovieList =
        this.copy(listMovie = this.listMovie.map { movie ->
            movie.copy(
                isFavourite = favoriteMoviesRepository.isMovieFavorite(
                    movie.id
                )
            )
        })

}