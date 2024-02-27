package com.example.androidjetpack.presentation

import androidx.lifecycle.ViewModel
import com.example.androidjetpack.presentation.loading_state.LoadViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _currentState = MutableStateFlow(LoadViewState.NONE)
    val currentState = _currentState.asStateFlow()
}