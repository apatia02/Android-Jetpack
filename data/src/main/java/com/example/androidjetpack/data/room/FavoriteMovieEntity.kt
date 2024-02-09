package com.example.androidjetpack.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность для избранных фильмов
 */
@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val movieId: Int
)