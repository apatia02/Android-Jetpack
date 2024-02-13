package com.example.androidjetpack.data.di

import android.content.Context
import androidx.room.Room
import com.example.androidjetpack.data.room.AppDatabase
import com.example.androidjetpack.data.room.RoomConstants.NAME_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Модуль для Room
 */
@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            NAME_DB
        ).build()
    }
}