package com.example.androidjetpack.presentation.loading_state

import android.view.LayoutInflater
import com.example.androidjetpack.presentation.databinding.LayoutNothingFoundStateBinding

/**
 * Представление состояния не найдено
 */
class NotFoundStatePresentation(
    private val placeHolder: PlaceHolderViewContainer,
    loadingText: String
) : LoadStatePresentation {

    private val binding by lazy {
        LayoutNothingFoundStateBinding.inflate(
            LayoutInflater.from(placeHolder.context), placeHolder, false
        )
    }

    init {
        binding.nothingFoundTv.text = loadingText
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