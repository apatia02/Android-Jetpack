package com.example.androidjetpack.presentation.loading_state

import android.view.LayoutInflater
import com.example.androidjetpack.presentation.databinding.LayoutLoadingStateBinding

/**
 * Представление состояния загрузки
 */
class TransparentLoadingStatePresentation(
    private val placeHolder: PlaceHolderViewContainer
) : LoadStatePresentation {

    private val binding by lazy {
        LayoutLoadingStateBinding.inflate(
            LayoutInflater.from(placeHolder.context), placeHolder, false
        )
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