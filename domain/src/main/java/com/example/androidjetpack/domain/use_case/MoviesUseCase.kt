package com.example.androidjetpack.domain.use_case

import com.example.androidjetpack.domain.EMPTY_STRING
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
     * Запрос на получение фильмов по фильтру, если фильтр пустой возвращает все фильмы
     */
    suspend fun getMovies(query: String): MovieList {
        val listMovie = if (query == EMPTY_STRING) {
            moviesRepository.getMovies()
        } else {
            moviesRepository.searchMovies(query)
        }
        return listMovie.setFavoriteStatusForMovies()
    }

    /**
     * Установка у фильмов статуса избранности
     */
    private suspend fun MovieList.setFavoriteStatusForMovies(): MovieList =
        this.copy(listMovie = this.listMovie.map { movie ->
            movie.copy(
                isFavourite = favoriteMoviesRepository.isMovieFavorite(movie.id)
            )
        })
}