package com.example.androidjetpack.domain.use_case

import com.example.androidjetpack.domain.repository.SettingsRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    operator fun invoke(mode: Int): Unit = settingsRepository.setThemeMode(mode)
}