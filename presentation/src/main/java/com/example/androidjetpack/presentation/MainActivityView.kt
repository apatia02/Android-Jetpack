package com.example.androidjetpack.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.example.androidjetpack.domain.use_case.GetThemeUseCase
import com.example.androidjetpack.presentation.databinding.ActivityMainViewBinding
import com.example.androidjetpack.presentation.fragments.MoviesFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivityView : AppCompatActivity() {

    @Inject
    lateinit var getThemeUseCase: GetThemeUseCase

    private lateinit var binding: ActivityMainViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        AppCompatDelegate.setDefaultNightMode(getThemeUseCase.getTheme())
        setBarsTransparent()
        addMoviesFragment()
    }

    @SuppressLint("CommitTransaction")
    private fun addMoviesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MoviesFragment())
            .commit()
    }

    private fun setBarsTransparent() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
    }
}