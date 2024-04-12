package com.example.androidjetpack.presentation.view_models

import androidx.lifecycle.ViewModel
import com.example.androidjetpack.domain.use_case.GetThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase
) : ViewModel() {


    fun getThemeMode(): Int = getThemeUseCase.invoke()
}