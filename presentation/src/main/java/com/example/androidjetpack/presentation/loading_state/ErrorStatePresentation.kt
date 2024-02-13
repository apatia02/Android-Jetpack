package com.example.androidjetpack.presentation.loading_state

import android.view.LayoutInflater
import com.example.androidjetpack.presentation.databinding.ErrorStateBinding

/**
 * Представление состояния ошибки
 */
class ErrorStatePresentation(
    private val placeHolder: PlaceHolderViewContainer,
    private val retryClick: () -> Unit,
) : LoadStatePresentation {

    private val binding by lazy {
        ErrorStateBinding.inflate(
            LayoutInflater.from(placeHolder.context), placeHolder, false
        )
    }

    init {
        binding.reloadIv.setOnClickListener { retryClick() }
    }

    override fun showState() {
        with(placeHolder) {
            changeViewTo(binding.root)
            setClickableAndFocusable(true)
            show()
        }
    }

    override fun hideState() {
        placeHolder.hide()
    }
}