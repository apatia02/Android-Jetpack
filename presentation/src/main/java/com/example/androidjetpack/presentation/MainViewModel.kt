package com.example.androidjetpack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.MovieList
import com.example.androidjetpack.domain.use_case.ChangeFavouriteStatusUseCase
import com.example.androidjetpack.domain.use_case.MoviesUseCase
import com.example.androidjetpack.presentation.UiConstants.PROGRESS_FINISH
import com.example.androidjetpack.presentation.UiConstants.PROGRESS_STEP
import com.example.androidjetpack.presentation.loading_state.LoadViewState.ERROR
import com.example.androidjetpack.presentation.loading_state.LoadViewState.LOADING
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NONE
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NOTHING_FOUND
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
    private val moviesUseCase: MoviesUseCase,
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase
) : ViewModel() {

    private val _currentState = MutableStateFlow(NONE)
    val currentState = _currentState.asStateFlow()

    private val _movies = MutableStateFlow(MovieList())
    val movies = _movies.asStateFlow()

    private val _progressRequest = MutableStateFlow(0)
    val progressRequest = _progressRequest.asStateFlow()

    private val _snackError = MutableStateFlow(false)
    val snackError = _snackError.asStateFlow()

    private val _query = MutableStateFlow(EMPTY_STRING)
    val query = _query.asStateFlow()

    private val _swrIsVisible = MutableStateFlow(false)
    val swrIsVisible = _swrIsVisible.asStateFlow()

    val hasData: Boolean
        get() = _movies.value.listMovie.isNotEmpty()

    suspend fun getMovies(query: String, isSwr: Boolean = false) {
        if (hasData) {
            getMoviesHasData(query, isSwr)
        } else {
            getMoviesHasNotData(query)
        }
    }

    fun setNewQuery(query: String) {
        _query.value = query
    }

    private suspend fun getMoviesHasNotData(query: String) {
        try {
            _currentState.value = LOADING
            _movies.value = moviesUseCase.getMovies(query)
            if (hasData) {
                _currentState.value = NONE
            } else {
                _currentState.value = NOTHING_FOUND
            }
        } catch (e: Exception) {
            _currentState.value = ERROR
        }
    }

    private suspend fun getMoviesHasData(query: String, isSwr: Boolean) {
        try {
            startProgress()
            if (isSwr) {
                _swrIsVisible.value = true
            }
            withContext(Dispatchers.IO) {
                _movies.value = moviesUseCase.getMovies(query)
            }
            if (!hasData) {
                _currentState.value = NOTHING_FOUND
            }
        } catch (e: Exception) {
            showSnackError()
        } finally {
            _progressRequest.value = PROGRESS_FINISH
            _swrIsVisible.value = false
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

    fun changeFavouriteStatus(movieId: Int) {
        viewModelScope.launch {
            changeFavouriteStatusUseCase.changeFavouriteStatus(movieId)
        }
    }
}