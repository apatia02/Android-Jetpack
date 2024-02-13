package com.example.androidjetpack.data.movie

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Дао для избранных фильмов
 */
@Dao
internal interface FavoriteMoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteMovie(favoriteMovie: FavoriteMovieEntity)

    @Delete
    suspend fun removeFavoriteMovie(favoriteMovie: FavoriteMovieEntity)

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_movies WHERE movieId = :movieId)")
    suspend fun checkIsMovieFavorite(movieId: Int): Boolean
}