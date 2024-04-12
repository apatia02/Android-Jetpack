package com.example.androidjetpack.presentation.loading_state

/**
 * Представление состояния загрузки
 */
enum class LoadViewState {
    NONE,
    MAIN_LOADING,
    TRANSPARENT_LOADING,
    NOTHING_FOUND,
    ERROR,
}