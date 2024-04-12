package com.example.androidjetpack.domain.use_case

import com.example.androidjetpack.domain.repository.SettingsRepository
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    operator fun invoke(): Int = settingsRepository.getThemeMode()
}