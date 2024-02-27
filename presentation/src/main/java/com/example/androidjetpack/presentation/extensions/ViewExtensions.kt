package com.example.androidjetpack.presentation.extensions

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/**
 * метод, получающий inset status bar и увеличивающий padding для view по этому inset
 */
fun View.insetStatusBar() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val inset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
        view.updatePadding(top = inset.top)
        insets
    }
}

/**
 * метод, получающий inset клавиатуры и увеличивающий padding для view по этому inset
 */
fun View.insetKeyBoard() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val inset = insets.getInsets(WindowInsetsCompat.Type.ime())
        view.updatePadding(bottom = inset.bottom)
        insets
    }
}