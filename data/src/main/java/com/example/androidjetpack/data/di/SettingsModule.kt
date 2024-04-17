package com.example.androidjetpack.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.androidjetpack.data.Constants.SHARED_NAME
import com.example.androidjetpack.data.repository.SettingsRepositoryImpl
import com.example.androidjetpack.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SettingsModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(sharedPreferences: SharedPreferences): SettingsRepository {
        return SettingsRepositoryImpl(sharedPreferences)
    }
}