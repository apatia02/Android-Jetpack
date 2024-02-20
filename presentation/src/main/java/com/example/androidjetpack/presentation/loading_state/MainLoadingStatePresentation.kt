package com.example.androidjetpack.presentation.loading_state

import android.view.LayoutInflater
import com.example.androidjetpack.presentation.databinding.LayoutMainLoadingStateBinding

/**
 * Представление состояния загрузки в виде обычного прогрес бара
 */
class MainLoadingStatePresentation(
    private val placeHolder: PlaceHolderViewContainer
) : LoadStatePresentation {

    private val binding by lazy {
        LayoutMainLoadingStateBinding.inflate(
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