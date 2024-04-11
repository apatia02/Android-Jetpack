package com.example.androidjetpack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.MovieList
import com.example.androidjetpack.domain.use_case.MoviesUseCase
import com.example.androidjetpack.presentation.loading_state.LoadViewState.ERROR
import com.example.androidjetpack.presentation.loading_state.LoadViewState.MAIN_LOADING
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NONE
import com.example.androidjetpack.presentation.loading_state.LoadViewState.TRANSPARENT_LOADING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moviesUseCase: MoviesUseCase
) : ViewModel() {

    private val _currentLoadState = MutableStateFlow(NONE)
    val currentLoadState = _currentLoadState.asStateFlow()

    private val _movies = MutableStateFlow(MovieList())
    val movies = _movies.asStateFlow()

    private val _snackError = MutableSharedFlow<Unit>()
    val snackError = _snackError.asSharedFlow()

    private val hasData: Boolean
        get() = _movies.value.listMovie.isNotEmpty()

    init {
        viewModelScope.launch {
            getMovies(EMPTY_STRING)
        }
    }

    suspend fun getMovies(query: String) {
        try {
            if (hasData) {
                _currentLoadState.value = TRANSPARENT_LOADING
            } else {
                _currentLoadState.value = MAIN_LOADING
            }
            _movies.value = moviesUseCase.getMovies(query)
            _currentLoadState.value = NONE
        } catch (e: Exception) {
            if (hasData) {
                showSnackError()
            } else {
                _currentLoadState.value = ERROR
            }
        }
    }

    private fun showSnackError() {
        viewModelScope.launch {
            _snackError.emit(Unit)
        }
    }
}