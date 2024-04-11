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
import com.example.androidjetpack.presentation.loading_state.LoadViewState.SWR_IS_NOT_VISIBLE
import com.example.androidjetpack.presentation.loading_state.LoadViewState.TRANSPARENT_LOADING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private val _query = MutableStateFlow(EMPTY_STRING)
    private val query = _query.asStateFlow()

    private val hasData: Boolean
        get() = _movies.value.listMovie.isNotEmpty()

    private var queryJob: Job? = null

    init {
        initObservers()
    }

    fun setNewQuery(query: String) {
        queryJob?.cancel()
        queryJob = null
        queryJob = viewModelScope.launch {
            if (query != _query.value) {
                delay(UiConstants.TIMEOUT_FILTER)
                _query.value = query
            }
        }
    }

    private fun initObservers() {
        viewModelScope.launch {
            query.collect {
                getMovies(query.value)
            }
        }
    }

    suspend fun getMovies(isSwr: Boolean = false) {
        try {
            when {
                isSwr -> _currentState.value = NONE
                hasData -> _currentState.value = TRANSPARENT_LOADING
                else -> _currentState.value = MAIN_LOADING
            }
            _movies.value = moviesUseCase.getMovies(query.value)
            if (hasData) {
                _currentLoadState.value = NONE
            } else {
                _currentLoadState.value = NOTHING_FOUND
            }
        } catch (e: Exception) {
            if (hasData) {
                showSnackError()
                _currentState.value = NONE
            } else {
                _currentLoadState.value = ERROR
            }
        } finally {
            if (isSwr) {
                _currentState.value = SWR_IS_NOT_VISIBLE
            }
        }
    }

    private fun showSnackError() {
        viewModelScope.launch {
            _snackError.emit(Unit)
        }
    }
}