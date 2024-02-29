package com.example.androidjetpack.presentation.extensions

import android.content.res.Resources

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.toPx: Float
    get() = (this * Resources.getSystem().displayMetrics.density)