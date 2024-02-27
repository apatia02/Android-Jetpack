package com.example.androidjetpack.domain.repository

interface SettingsRepository {

    fun getThemeMode(): Int

    fun setThemeMode(mode: Int)
}