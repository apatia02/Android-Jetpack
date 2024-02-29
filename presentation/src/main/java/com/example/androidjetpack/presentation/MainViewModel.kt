package com.example.androidjetpack.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.domain.use_case.ChangeFavouriteStatusUseCase
import com.example.androidjetpack.domain.use_case.GetFavouriteStatusUseCase
import com.example.androidjetpack.domain.use_case.GetThemeUseCase
import com.example.androidjetpack.domain.use_case.MoviesUseCase
import com.example.androidjetpack.domain.use_case.SetThemeUseCase
import com.example.androidjetpack.presentation.UiConstants.PAGE_SIZE
import com.example.androidjetpack.presentation.loading_state.LoadViewState
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NONE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moviesUseCase: MoviesUseCase,
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase,
    private val getFavouriteStatusUseCase: GetFavouriteStatusUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : ViewModel() {

    private val _currentState = MutableStateFlow(NONE)
    val currentState = _currentState.asStateFlow()

    private val _movies = MutableStateFlow(PagingData.empty<Movie>())
    val movies = _movies.asStateFlow()

    private val _query = MutableStateFlow(EMPTY_STRING)
    val query = _query.asStateFlow()

    private var loadDataJob: Job? = null

    var scrollPosition = 0

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

    private suspend fun getPagingMovies(query: String) {
        val pager = Pager(config = PagingConfig(pageSize = PAGE_SIZE)) {
            MoviePagingSource(moviesUseCase, query)
        }.flow.cachedIn(viewModelScope)
        _movies.value = pager.first()
    }

    fun getFavouriteStatus(movieId: Int): Boolean = runBlocking {
        getFavouriteStatusUseCase.invoke(movieId)
    }

    fun setTheme(mode: Int): Unit = setThemeUseCase.invoke(mode)

    fun getTheme(): Int = getThemeUseCase.invoke()

    fun changeFavouriteStatus(movieId: Int) {
        viewModelScope.launch {
            changeFavouriteStatusUseCase.invoke(movieId)
        }
    }
}