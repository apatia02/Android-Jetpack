package com.example.androidjetpack.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidjetpack.base_resources.R.drawable
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.domain.use_case.GetFavouriteStatusUseCase
import com.example.androidjetpack.presentation.databinding.LayoutItemFilmBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieAdapter(
    private val getFavouriteStatusUseCase: GetFavouriteStatusUseCase,
    private val onClickListener: (String) -> Unit, private val changeFavouriteStatus: (Int) -> Unit,
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
            holder.bind(
                movie = movie,
                getFavouriteStatusUseCase = getFavouriteStatusUseCase,
                onClickListener = onClickListener,
                changeFavouriteStatus = changeFavouriteStatus,
            )
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

        private var heartRes = drawable.heart_outlined

        fun bind(
            movie: Movie,
            getFavouriteStatusUseCase: GetFavouriteStatusUseCase,
            onClickListener: (String) -> Unit,
            changeFavouriteStatus: (Int) -> Unit,
        ) = with(binding) {
            titleTv.text = movie.title
            descriptionTv.text = movie.description
            CoroutineScope(Dispatchers.Default).launch {
                heartRes =
                    if (getFavouriteStatusUseCase.getFavouriteStatus(movie.id)) drawable.heart_filled
                    else drawable.heart_outlined
                heartIv.setImageResource(heartRes)
            }
            Glide.with(itemView).load(movie.posterPath).placeholder(drawable.placeholder)
                .into(posterIv)
            dateTv.text = movie.releaseDate
            container.setOnClickListener { onClickListener(movie.title) }
            heartIv.setOnClickListener {
                clickOnHeart(movie.id, changeFavouriteStatus)
            }
        }

        private fun clickOnHeart(movieId: Int, changeFavouriteStatus: (Int) -> Unit) =
            with(binding) {
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