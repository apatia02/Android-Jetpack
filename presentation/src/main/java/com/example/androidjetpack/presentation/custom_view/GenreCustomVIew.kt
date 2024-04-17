package com.example.androidjetpack.presentation.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import com.example.androidjetpack.base_resources.R.drawable
import com.example.androidjetpack.base_resources.R.style
import com.example.androidjetpack.domain.entity.Genre
import com.example.androidjetpack.presentation.extensions.toPx
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout

/**
 * CustomView для отображения жанра фильма
 */
class GenreCustomVIew @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FlexboxLayout(context, attrs, defStyleAttr) {

    private val skillsFlexbox: FlexboxLayout

    init {
        skillsFlexbox = FlexboxLayout(context)
        skillsFlexbox.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        skillsFlexbox.flexWrap = FlexWrap.WRAP
        addView(skillsFlexbox)
    }

    fun setGenres(genres: List<Genre>) {
        skillsFlexbox.removeAllViews()
        for (genre in genres) {
            val genreTv = createGenre(genre.name)
            skillsFlexbox.addView(genreTv)
        }
    }

    private fun createGenre(name: String): TextView = TextView(context).apply {
        text = name
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply { updateMargins(right = innerMargin) }

        setTextAppearance(style.Text_Description)
        background = AppCompatResources.getDrawable(context, drawable.bg_genre)
        gravity = Gravity.CENTER
        updatePadding(
            horizontalPadding, verticalPadding, horizontalPadding, verticalPadding
        )
    }

    private companion object {
        val horizontalPadding = 12.toPx
        val verticalPadding = 5.toPx
        val innerMargin = 8.toPx
    }
}