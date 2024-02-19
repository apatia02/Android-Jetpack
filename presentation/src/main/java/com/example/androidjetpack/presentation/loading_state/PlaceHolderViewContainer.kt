package com.example.androidjetpack.presentation.loading_state

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val ALPHA_FULL = 1f
private const val DEFAULT_DURATION = 0L
private const val STATE_TOGGLE_DELAY_MS = 250L

/**
 * Контейнер для отображения состояний загрузки
 */
class PlaceHolderViewContainer(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    private val loadStateFlow = MutableStateFlow<StatePresentation?>(null)
    private var stateJob: Job? = null

    init {
        moveOnTop()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        stateJob = GlobalScope.launch(Dispatchers.Main) {
            loadStateFlow
                .debounce(STATE_TOGGLE_DELAY_MS)
                .distinctUntilChanged()
                .collect { state ->
                    state?.let {
                        removeAllViews()
                        addView(it.stateView)
                    }
                }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        stateJob?.cancel()
        stateJob = null
    }

    fun changeViewTo(view: View) {
        loadStateFlow.value = StatePresentation(view)
    }

    fun show() {
        fadeIn(DEFAULT_DURATION, ALPHA_FULL)
    }

    fun hide() {
        fadeOut(DEFAULT_DURATION, View.GONE, ALPHA_FULL)
    }

    private fun moveOnTop() {
        elevation = Float.MAX_VALUE
    }
}

fun PlaceHolderViewContainer.setClickableAndFocusable(value: Boolean) {
    isClickable = value
    isFocusable = value
    isFocusableInTouchMode = value
}

private fun View.fadeIn(duration: Long, alpha: Float) {
    animate()
        .alpha(alpha)
        .setDuration(duration)
        .setListener(null)
}

private fun View.fadeOut(duration: Long, visibility: Int, alpha: Float) {
    animate()
        .alpha(alpha)
        .setDuration(duration)
        .withEndAction { this.visibility = visibility }
}