package com.example.androidjetpack.presentation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.lifecycle.lifecycleScope
import com.example.androidjetpack.base_resources.R.string
import com.example.androidjetpack.presentation.databinding.ActivityMainViewBinding
import com.example.androidjetpack.presentation.loading_state.ErrorStatePresentation
import com.example.androidjetpack.presentation.loading_state.LoadStatePresentation
import com.example.androidjetpack.presentation.loading_state.LoadViewState
import com.example.androidjetpack.presentation.loading_state.NotFoundStatePresentation
import com.example.androidjetpack.presentation.loading_state.TransparentLoadingStatePresentation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivityView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding

    private var loadStatePresentation: LoadStatePresentation? = null

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.container.insetKeyBoardMargin()
        setObservers()
    }

    /**
     * метод, получающий inset клавиатуры и увеличивающий margin для view по этому inset
     */
    private fun View.insetKeyBoardMargin() {
        val initialMargin = this.marginBottom
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            val insetKeyBoard = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val insetNavBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val margin = if (insetKeyBoard == 0) {
                initialMargin
            } else {
                insetKeyBoard + initialMargin - insetNavBar
            }

            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                bottomMargin = margin
                view.layoutParams = this
            }
            insets
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.currentState.collect { currentState ->
                updateStatePresentation(currentState)
            }
        }
    }

    private fun updateStatePresentation(currentState: LoadViewState) = with(binding) {
        when (currentState) {
            LoadViewState.NONE -> {
                loadStatePresentation?.hideState()
            }

            LoadViewState.LOADING -> {
                loadStatePresentation = TransparentLoadingStatePresentation(placeHolder)
                loadStatePresentation?.showState()
            }

            LoadViewState.NOTHING_FOUND -> {
                loadStatePresentation = NotFoundStatePresentation(
                    placeHolder,
                    getString(string.nothing_found_message, filterEt.text)
                )
                loadStatePresentation?.showState()
            }

            LoadViewState.ERROR -> {
                loadStatePresentation = ErrorStatePresentation(placeHolder) {
                    //todo добавить повторную загрузку данных
                }
                loadStatePresentation?.showState()
            }
        }
    }
}