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
import com.example.androidjetpack.presentation.loading_state.LoadViewState
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
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

    private val _currentLoadState = MutableStateFlow(NONE)
    val currentLoadState = _currentLoadState.asStateFlow()

    private val _movies = MutableStateFlow(PagingData.empty<Movie>())
    val movies = _movies.asStateFlow()

    private val _query = MutableStateFlow(EMPTY_STRING)
    private val query = _query.asStateFlow()

    private var loadDataJob: Job? = null

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
                getMovies()
            }
        }
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

    private suspend fun getPagingMovies(query: String) {
        val pager = Pager(config = PagingConfig(pageSize = PAGE_SIZE)) {
            MoviePagingSource(moviesUseCase, query)
        }.flow
        _movies.value = pager.first()
    }

    fun changeFavouriteStatus(movieId: Int) {
        viewModelScope.launch {
            changeFavouriteStatusUseCase.changeFavouriteStatus(movieId)
            _movies.value = movies.value.changeFavouriteStatus(movieId)
        }
    }

    private fun MovieList.changeFavouriteStatus(movieId: Int): MovieList {
        return copy(listMovie = listMovie.map { movie ->
            if (movie.id == movieId) {
                movie.copy(isFavourite = !movie.isFavourite)
            } else {
                movie
            }
        })
    }
}