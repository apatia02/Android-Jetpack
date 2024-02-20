package com.example.androidjetpack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.MovieList
import com.example.androidjetpack.domain.use_case.MoviesUseCase
import com.example.androidjetpack.presentation.loading_state.LoadViewState.ERROR
import com.example.androidjetpack.presentation.loading_state.LoadViewState.MAIN_LOADING
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NONE
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NOTHING_FOUND
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

    private val _currentState = MutableStateFlow(NONE)
    val currentState = _currentState.asStateFlow()

    private val _movies = MutableStateFlow(MovieList())
    val movies = _movies.asStateFlow()

    private val _snackError = MutableSharedFlow<Unit>()
    val snackError = _snackError.asSharedFlow()

    private val _query = MutableStateFlow(EMPTY_STRING)
    val query = _query.asStateFlow()

    private val hasData: Boolean
        get() = _movies.value.listMovie.isNotEmpty()

    fun setNewQuery(query: String) {
        _query.value = query
    }

    suspend fun getMovies(query: String = _query.value) {
        try {
            if (hasData) {
                _currentState.value = TRANSPARENT_LOADING
            } else {
                _currentState.value = MAIN_LOADING
            }
            _movies.value = moviesUseCase.getMovies(query)
            if (hasData) {
                _currentState.value = NONE
            } else {
                _currentState.value = NOTHING_FOUND
            }
        } catch (e: Exception) {
            if (hasData) {
                showSnackError()
            } else {
                _currentState.value = ERROR
            }
        }
    }

    private fun showSnackError() {
        viewModelScope.launch {
            _snackError.emit(Unit)
        }
    }
}