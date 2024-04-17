package com.example.androidjetpack.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.androidjetpack.domain.repository.SettingsRepository
import javax.inject.Inject

internal class SettingsRepositoryImpl @Inject constructor(private val shared: SharedPreferences) :
    SettingsRepository {

    override fun getThemeMode(): Int =
        shared.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_NO)

    override fun setThemeMode(mode: Int): Unit = shared.edit().putInt(KEY_THEME_MODE, mode).apply()

    private companion object {
        private const val KEY_THEME_MODE = "theme mode"
    }
}