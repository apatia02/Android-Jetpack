package com.example.androidjetpack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.domain.use_case.ChangeFavouriteStatusUseCase
import com.example.androidjetpack.domain.use_case.GetMoviesUseCase
import com.example.androidjetpack.domain.use_case.GetThemeUseCase
import com.example.androidjetpack.domain.use_case.SetThemeUseCase
import com.example.androidjetpack.presentation.UiConstants.PAGE_SIZE
import com.example.androidjetpack.presentation.loading_state.LoadViewState.ERROR
import com.example.androidjetpack.presentation.loading_state.LoadViewState.MAIN_LOADING
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NONE
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NOTHING_FOUND
import com.example.androidjetpack.presentation.loading_state.LoadViewState.TRANSPARENT_LOADING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    private val _currentLoadState = MutableStateFlow(NONE)
    val currentLoadState = _currentLoadState.asStateFlow()

    private val _currentAdapterState = MutableStateFlow<LoadState>(LoadState.Loading)
    private val currentAdapterState = _currentAdapterState.asStateFlow()

    private val _movies = MutableStateFlow(PagingData.empty<Movie>())
    val movies = _movies.asStateFlow()

    private val _snackError = MutableSharedFlow<Unit>()
    val snackError = _snackError.asSharedFlow()

    private val _isSwrVisible = MutableStateFlow(false)
    val isSwrVisible = _isSwrVisible.asStateFlow()

    private val _query = MutableStateFlow(EMPTY_STRING)
    private val query = _query.asStateFlow()

    var hasData = false

    var scrollPosition = 0

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
                getPagingMovies()
            }
        }
        viewModelScope.launch {
            currentAdapterState.collect {
                renderAdapterState(it)
            }
        }
    }

    fun refreshData() {
        loadDataJob?.cancel()
        loadDataJob = null
        loadDataJob = viewModelScope.launch {
            getPagingMovies(query.value)
        }
    }

    fun setAdapterState(state: LoadState) {
        _currentAdapterState.value = state
    }

    fun setSwrVisible() {
        _isSwrVisible.value = true
    }

    private fun renderAdapterState(state: LoadState) {
        when (state) {
            is LoadState.Loading -> {
                when {
                    isSwrVisible.value -> {
                        _currentLoadState.value = NONE
                    }

                    hasData -> {
                        _currentLoadState.value = TRANSPARENT_LOADING
                    }

                    else -> {
                        _currentLoadState.value = MAIN_LOADING
                    }
                }
            }

            is LoadState.Error -> {
                if (hasData) {
                    viewModelScope.launch {
                        _snackError.emit(Unit)
                    }
                } else {
                    _currentLoadState.value = ERROR
                }
                if (isSwrVisible.value) {
                    _isSwrVisible.value = false
                }
            }

            is LoadState.NotLoading -> {
                if (isSwrVisible.value) {
                    _isSwrVisible.value = false
                }
                if (currentLoadState.value != NONE) {
                    if (hasData) {
                        _currentLoadState.value = NONE
                    } else {
                        _currentLoadState.value = NOTHING_FOUND
                    }
                }
            }
        }
    }

    private suspend fun getPagingMovies(query: String = _query.value) {
        val pager = Pager(config = PagingConfig(pageSize = PAGE_SIZE)) {
            MoviePagingSource(getMoviesUseCase, query)
        }.flow.cachedIn(viewModelScope)
        _movies.value = pager.first()
    }

    fun setTheme(mode: Int): Unit = setThemeUseCase.invoke(mode)

    fun getTheme(): Int = getThemeUseCase.invoke()

    fun changeFavouriteStatus(movieId: Int) {
        viewModelScope.launch {
            changeFavouriteStatusUseCase.invoke(movieId)
            _movies.value = movies.value.map { movie ->
                if (movie.id == movieId) {
                    movie.copy(isFavourite = !movie.isFavourite)
                } else {
                    movie
                }
            }
        }
    }
}