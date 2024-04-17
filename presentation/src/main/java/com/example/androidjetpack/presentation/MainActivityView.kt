package com.example.androidjetpack.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.example.androidjetpack.presentation.databinding.ActivityMainViewBinding
import com.example.androidjetpack.presentation.fragments.MoviesFragment
import com.example.androidjetpack.presentation.view_models.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        AppCompatDelegate.setDefaultNightMode(viewModel.getThemeMode())
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