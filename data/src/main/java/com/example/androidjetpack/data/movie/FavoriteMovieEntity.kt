package com.example.androidjetpack.data.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность для избранных фильмов
 */
@Entity(tableName = "favorite_movies")
internal data class FavoriteMovieEntity(
    @PrimaryKey val movieId: Int
)