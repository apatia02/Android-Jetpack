package com.example.androidjetpack.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidjetpack.presentation.databinding.ActivityMainViewBinding
import dagger.hilt.android.AndroidEntryPoint

class MainActivityView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}