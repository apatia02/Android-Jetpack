package com.example.androidjetpack.presentation.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidjetpack.domain.entity.GenreList
import com.example.androidjetpack.domain.use_case.ChangeFavouriteStatusUseCase
import com.example.androidjetpack.domain.use_case.GetGenresWithIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val changeFavouriteStatusUseCase: ChangeFavouriteStatusUseCase,
    private val getGenresWithIdUseCase: GetGenresWithIdUseCase
) : ViewModel() {

    private val _genres = MutableStateFlow(GenreList())
    val genres = _genres.asStateFlow()

    private val _isFavourite = MutableStateFlow(false)
    val isFavourite = _isFavourite.asStateFlow()

    fun setGenres(listGenreId: List<Int>) {
        viewModelScope.launch {
            _genres.value = getGenresWithIdUseCase.invoke(listGenreId)
        }
    }

    fun setInitialStatus(isFavourite: Boolean) {
        _isFavourite.value = isFavourite
    }

    fun changeFavouriteStatus(movieId: Int) {
        _isFavourite.value = !_isFavourite.value
        viewModelScope.launch {
            changeFavouriteStatusUseCase.invoke(movieId)
        }
    }
}