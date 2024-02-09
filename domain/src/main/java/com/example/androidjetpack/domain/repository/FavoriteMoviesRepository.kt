package com.example.androidjetpack.domain.repository

/**
 * Интерфейс для репозитория, свзянного с избранными фильмами
 */
interface FavoriteMoviesRepository {
    suspend fun addFavoriteMovie(movieId: Int)
    suspend fun removeFavoriteMovie(movieId: Int)
    suspend fun isMovieFavorite(movieId: Int): Boolean
}