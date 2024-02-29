package com.example.androidjetpack.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidjetpack.domain.entity.GenreList
import com.example.androidjetpack.domain.use_case.ChangeFavouriteStatusUseCase
import com.example.androidjetpack.domain.use_case.GenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase,
    private val genresUseCase: GenresUseCase
) : ViewModel() {

    private val _genres = MutableStateFlow(GenreList())
    val genres = _genres.asStateFlow()

    fun setGenres(listGenreId: List<Int>) {
        viewModelScope.launch {
            _genres.value = genresUseCase.getGenresFilteredWithId(listGenreId)
        }
    }

    fun changeFavouriteStatus(movieId: Int) {
        viewModelScope.launch {
            changeFavouriteStatusUseCase.changeFavouriteStatus(movieId)
        }
    }
}