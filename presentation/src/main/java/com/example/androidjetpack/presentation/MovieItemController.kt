package com.example.androidjetpack.presentation

import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.androidjetpack.base_resources.R.drawable
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.presentation.databinding.LayoutItemFilmBinding
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder

class MovieItemController() :
    BindableItemController<Movie, MovieItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(movie: Movie): String = movie.id.toString()

    inner class Holder(
        parent: ViewGroup
    ) : BindableViewHolder<Movie>(parent, R.layout.layout_item_film) {

        private val binding = LayoutItemFilmBinding.bind(itemView)

        override fun bind(movie: Movie): Unit = with(binding) {
            titleTv.text = movie.title
            descriptionTv.text = movie.description
            val heartRes = if (movie.isFavourite) drawable.heart_filled else drawable.heart_outlined
            heartIv.setImageResource(heartRes)
            Glide.with(itemView)
                .load(movie.posterPath)
                .placeholder(drawable.placeholder)
                .into(posterIv)
            dateTv.text = movie.releaseDate
        }
    }
}