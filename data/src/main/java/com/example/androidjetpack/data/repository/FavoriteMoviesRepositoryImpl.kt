package com.example.androidjetpack.data.repository

import com.example.androidjetpack.data.movie.FavoriteMovieEntity
import com.example.androidjetpack.data.movie.FavoriteMoviesDao
import com.example.androidjetpack.domain.repository.FavoriteMoviesRepository
import javax.inject.Inject

/**
 * Репозиторий, свзянный с избранными фильмами
 */
internal class FavoriteMoviesRepositoryImpl @Inject constructor(
    private val favoriteMoviesDao: FavoriteMoviesDao
) : FavoriteMoviesRepository {

    override suspend fun addFavoriteMovie(movieId: Int) {
        val favoriteMovie = FavoriteMovieEntity(movieId)
        favoriteMoviesDao.addFavoriteMovie(favoriteMovie)
    }

    override suspend fun removeFavoriteMovie(movieId: Int) {
        val favoriteMovie = FavoriteMovieEntity(movieId)
        favoriteMoviesDao.removeFavoriteMovie(favoriteMovie)
    }

    override suspend fun isMovieFavorite(movieId: Int): Boolean {
        return favoriteMoviesDao.checkIsMovieFavorite(movieId)
    }
}