package com.example.androidjetpack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.domain.use_case.ChangeFavouriteStatusUseCase
import com.example.androidjetpack.domain.use_case.MoviesUseCase
import com.example.androidjetpack.presentation.UiConstants.PAGE_SIZE
import com.example.androidjetpack.presentation.UiConstants.PROGRESS_FINISH
import com.example.androidjetpack.presentation.UiConstants.PROGRESS_STEP
import com.example.androidjetpack.presentation.loading_state.LoadViewState
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moviesUseCase: MoviesUseCase,
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase
) : ViewModel() {

    private val _currentState = MutableStateFlow(NONE)
    val currentState = _currentState.asStateFlow()

    private val _movies = MutableStateFlow(PagingData.empty<Movie>())
    val movies = _movies.asStateFlow()

    private val _query = MutableStateFlow(EMPTY_STRING)
    val query = _query.asStateFlow()

    private var loadDataJob: Job? = null

    private var startProgressJob: Job? = null

    fun setNewQuery(query: String) {
        _query.value = query
    }

    fun refreshData() {
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            getPagingMovies(query.value)
        }
    }

    fun setLoadingState(state: LoadViewState) {
        _currentState.value = state
    }

    fun finishLoading() {
        startProgressJob?.cancel()
        _progressRequest.value = PROGRESS_FINISH
        if (swrIsVisible.value) {
            _swrIsVisible.value = false
        }
    }

    private suspend fun getPagingMovies(query: String) {
        val pager = Pager(config = PagingConfig(pageSize = PAGE_SIZE)) {
            MoviePagingSource(moviesUseCase, query)
        }.flow
        _movies.value = pager.first()
    }

    fun startLoadingHasData() {
        startProgressJob?.cancel()
        startProgressJob = viewModelScope.launch {
            for (progress in 0 until PROGRESS_FINISH step PROGRESS_STEP) {
                _progressRequest.value = progress
                delay(100)
            }
        }
    }

    fun changeFavouriteStatus(movieId: Int) {
        viewModelScope.launch {
            changeFavouriteStatusUseCase.changeFavouriteStatus(movieId)
        }
    }
}