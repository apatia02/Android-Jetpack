package com.example.androidjetpack.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidjetpack.base_resources.R.drawable
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.presentation.databinding.LayoutItemFilmBinding

class MovieAdapter(
    private val onClickListener: (Movie) -> Unit, private val changeFavouriteStatus: (Int) -> Unit
) : PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback) {

    companion object {
        private val MovieDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie, onClickListener, changeFavouriteStatus)
        }
    }

    class MovieViewHolder private constructor(
        private val binding: LayoutItemFilmBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): MovieViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutItemFilmBinding.inflate(inflater, parent, false)
                return MovieViewHolder(binding)
            }
        }

        fun bind(
            movie: Movie, onClickListener: (Movie) -> Unit, changeFavouriteStatus: (Int) -> Unit
        ) = with(binding) {
            titleTv.text = movie.title
            descriptionTv.text = movie.description
            val heartRes = if (movie.isFavourite) drawable.heart_filled else drawable.heart_outlined
            heartIv.setImageResource(heartRes)
            Glide.with(itemView).load(movie.posterPath).placeholder(drawable.placeholder)
                .into(posterIv)
            dateTv.text = movie.releaseDate
            container.setOnClickListener { onClickListener(movie) }
            heartIv.setOnClickListener { changeFavouriteStatus(movie.id) }
        }
    }
}