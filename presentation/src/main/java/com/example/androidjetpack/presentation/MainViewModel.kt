package com.example.androidjetpack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.MovieList
import com.example.androidjetpack.domain.use_case.MoviesUseCase
import com.example.androidjetpack.presentation.UiConstants.PROGRESS_FINISH
import com.example.androidjetpack.presentation.UiConstants.PROGRESS_STEP
import com.example.androidjetpack.presentation.loading_state.LoadViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moviesUseCase: MoviesUseCase
) : ViewModel() {

    private val _currentState = MutableStateFlow(LoadViewState.NONE)
    val currentState = _currentState.asStateFlow()

    private val _movies = MutableStateFlow(MovieList())
    val movies = _movies.asStateFlow()

    private val _progressRequest = MutableStateFlow(0)
    val progressRequest = _progressRequest.asStateFlow()

    private val _snackError = MutableStateFlow(false)
    val snackError = _snackError.asStateFlow()

    val hasData: Boolean
        get() = _movies.value.listMovie.isNotEmpty()

    init {
        viewModelScope.launch {
            getMovies(EMPTY_STRING)
        }
    }

    suspend fun getMovies(query: String) {
        if (hasData) {
            getMoviesHasData(query)
        } else getMoviesHasNotData(query)
    }

    private suspend fun getMoviesHasNotData(query: String) {
        try {
            _currentState.value = LoadViewState.LOADING
            _movies.value = moviesUseCase.getMovies(query)
        } catch (e: Exception) {
            _currentState.value = LoadViewState.ERROR
        } finally {
            if (_currentState.value != LoadViewState.ERROR) {
                _currentState.value = LoadViewState.NONE
            }
        }
    }

    private suspend fun getMoviesHasData(query: String) {
        try {
            startProgress()
            withContext(Dispatchers.IO) {
                _movies.value = moviesUseCase.getMovies(query)
            }
            _progressRequest.value = PROGRESS_FINISH
        } catch (e: Exception) {
            showSnackError()
        }
    }

    private suspend fun startProgress() {
        for (progress in 0 until PROGRESS_FINISH step PROGRESS_STEP) {
            _progressRequest.value = progress
            delay(100)
        }
    }

    private fun showSnackError() {
        _snackError.value = true
        _snackError.value = false
    }
}