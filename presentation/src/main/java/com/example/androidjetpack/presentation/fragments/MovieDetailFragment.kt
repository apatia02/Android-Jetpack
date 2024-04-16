package com.example.androidjetpack.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.androidjetpack.base_resources.R
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.presentation.UiConstants.KEY_MOVIE_BUNDLE
import com.example.androidjetpack.presentation.UiConstants.RATING_FORMAT
import com.example.androidjetpack.presentation.databinding.FragmentMovieDetailBinding
import com.example.androidjetpack.presentation.view_models.DetailMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    private val viewModel: DetailMovieViewModel by viewModels()

    private lateinit var movie: Movie

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setMoviesValue()
        setListeners()
        setObserves()
        viewModel.setGenres(movie.genreIds)
    }

    private fun setMoviesValue() = with(binding) {
        movie = arguments?.getSerializable(KEY_MOVIE_BUNDLE) as Movie
        Glide.with(this@MovieDetailFragment)
            .load(movie.posterPath)
            .placeholder(R.drawable.placeholder)
            .into(posterIv)
        nameTv.text = movie.title
        descriptionTv.text = movie.description
        languageTv.text = movie.originalLanguage
        rateTv.text = getString(R.string.rating, String.format(RATING_FORMAT, movie.voteAverage))
        viewModel.setInitialStatus(movie.isFavourite)
    }

    private fun setListeners() = with(binding) {
        heartIv.setOnClickListener { viewModel.changeFavouriteStatus(movie.id) }
        arrowBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setObserves() = with(binding) {
        lifecycleScope.launchWhenStarted {
            viewModel.genres.collect {
                genresCv.setGenres(it.genres)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isFavourite.collect {
                val heartRes = if (it) {
                    R.drawable.heart_filled
                } else {
                    R.drawable.heart_outlined
                }
                heartIv.setImageResource(heartRes)
            }
        }
    }
}