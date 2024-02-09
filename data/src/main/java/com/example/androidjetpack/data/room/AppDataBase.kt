package com.example.androidjetpack.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room БД
 */
@Database(entities = [FavoriteMovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMoviesDao(): FavoriteMoviesDao
}