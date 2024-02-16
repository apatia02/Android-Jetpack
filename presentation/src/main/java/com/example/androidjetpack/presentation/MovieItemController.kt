package com.example.androidjetpack.presentation

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.androidjetpack.base_resources.R.drawable
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.presentation.databinding.ItemFilmBinding
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class MovieItemController(
    val onClickListener: (String) -> Unit,
    val changeFavouriteStatus: (Int) -> Unit
) :
    BindableItemController<Movie, MovieItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(movie: Movie): String = movie.id.toString()

    inner class Holder(
        parent: ViewGroup
    ) : BindableViewHolder<Movie>(parent, R.layout.item_film) {

        private val binding = ItemFilmBinding.bind(itemView)

        private var heartRes = drawable.heart_outlined

        override fun bind(movie: Movie): Unit = with(binding) {
            titleTv.text = movie.title
            descriptionTv.text = movie.description
            heartRes = if (movie.isFavourite) drawable.heart_filled else drawable.heart_outlined
            heartIv.setImageResource(heartRes)
            Glide.with(itemView)
                .load(movie.posterPath)
                .placeholder(drawable.placeholder)
                .into(posterIv)
            dateTv.text = movie.releaseDate
            container.setOnClickListener { onClickListener(movie.title) }
            heartIv.setOnClickListener {
                clickOnHeart(movie.id)
            }
        }

        private fun clickOnHeart(movieId: Int) = with(binding) {
            changeFavouriteStatus(movieId)
            heartRes = if (heartRes == drawable.heart_filled) {
                drawable.heart_outlined
            } else {
                drawable.heart_filled
            }
            heartIv.setImageResource(heartRes)
        }
    }
}