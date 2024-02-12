package com.example.androidjetpack.presentation

import android.text.TextUtils
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.bumptech.glide.Glide
import com.example.androidjetpack.base_resources.R.drawable
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.presentation.databinding.ItemFilmBinding
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MovieItemController() :
    BindableItemController<Movie, MovieItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(movie: Movie): String = movie.id.toString()

    inner class Holder(
        parent: ViewGroup
    ) : BindableViewHolder<Movie>(parent, R.layout.item_film) {

        private val binding = ItemFilmBinding.bind(itemView)

        override fun bind(movie: Movie):Unit = with(binding) {
            titleTv.text = movie.title
            descriptionTv.text = movie.description
            val heartRes = if (movie.isFavourite) drawable.heart_filled else drawable.heart_outlined
            heartIv.setImageResource(heartRes)
            Glide.with(itemView)
                .load(movie.posterPath)
                .into(posterIv)
            dateTv.text = formatRussianDate(movie.releaseDate)
            setDescriptionListener()
        }
        private fun formatRussianDate(inputDate: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
            val date = LocalDate.parse(inputDate, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
            return date.format(outputFormatter)
        }

        private fun setDescriptionListener() = with(binding.descriptionTv){
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val noOfLinesVisible: Int = height / lineHeight
                    maxLines = noOfLinesVisible
                    ellipsize = TextUtils.TruncateAt.END
                }
            })
        }
    }
}