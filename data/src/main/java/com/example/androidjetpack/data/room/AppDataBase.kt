package com.example.androidjetpack.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidjetpack.data.movie.FavoriteMovieEntity
import com.example.androidjetpack.data.movie.FavoriteMoviesDao

/**
 * Room БД
 */
@Database(entities = [FavoriteMovieEntity::class], version = 1, exportSchema = false)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMoviesDao(): FavoriteMoviesDao
}