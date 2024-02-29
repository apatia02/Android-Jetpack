package com.example.androidjetpack.presentation.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidjetpack.base_resources.R
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.presentation.UiConstants
import com.example.androidjetpack.presentation.UiConstants.KEY_MOVIE_BUNDLE
import com.example.androidjetpack.presentation.adapter.MovieAdapter
import com.example.androidjetpack.presentation.adapter.MovieLoadStateAdapter
import com.example.androidjetpack.presentation.databinding.FragmentMoviesBinding
import com.example.androidjetpack.presentation.extensions.insetKeyBoard
import com.example.androidjetpack.presentation.extensions.insetStatusBar
import com.example.androidjetpack.presentation.loading_state.ErrorStatePresentation
import com.example.androidjetpack.presentation.loading_state.LoadStatePresentation
import com.example.androidjetpack.presentation.loading_state.LoadViewState
import com.example.androidjetpack.presentation.loading_state.MainLoadingStatePresentation
import com.example.androidjetpack.presentation.loading_state.NotFoundStatePresentation
import com.example.androidjetpack.presentation.loading_state.TransparentLoadingStatePresentation
import com.example.androidjetpack.presentation.view_models.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    private var loadStatePresentation: LoadStatePresentation? = null

    private val viewModel: MainViewModel by viewModels()

    private lateinit var movieAdapter: MovieAdapter

    private val hasData: Boolean
        get() = movieAdapter.itemCount > 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onConfigurationChanged(newConfig: Configuration) = with(binding) {
        super.onConfigurationChanged(newConfig)
        saveScrollPosition()
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            moviesRv.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            moviesRv.layoutManager = LinearLayoutManager(requireContext())
        }
        moviesRv.scrollToPosition(viewModel.scrollPosition)
    }

    private fun init() {
        setRecyclerView()
        setInsets()
        setObservers()
        setListeners()
    }

    private fun setInsets() {
        binding.placeHolder.insetKeyBoard()
        binding.container.insetStatusBar()
    }

    private fun saveScrollPosition() {
        val layoutManager = binding.moviesRv.layoutManager
        viewModel.scrollPosition = when (layoutManager) {
            is GridLayoutManager -> {
                layoutManager.findFirstVisibleItemPosition()
            }

            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            else -> 0
        }
    }

    private fun setListeners() = with(binding) {
        filterEt.addTextChangedListener { newText ->
            viewModel.setNewQuery(newText.toString())
            clearFilterBtn.isGone = newText.toString().isEmpty()
        }
        clearFilterBtn.setOnClickListener { clearFilter() }
        swipeRefresh.setOnRefreshListener {
            viewModel.refreshData()
        }
        addAdapterListener()
        settingsIv.setOnClickListener { showThemeSelectionDialog() }
    }

    private fun addAdapterListener() = with(binding) {
        movieAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    when {
                        swipeRefresh.isRefreshing -> {
                            viewModel.setLoadingState(LoadViewState.NONE)
                        }

                        hasData -> {
                            viewModel.setLoadingState(LoadViewState.TRANSPARENT_LOADING)
                        }

                        else -> {
                            viewModel.setLoadingState(LoadViewState.MAIN_LOADING)
                        }
                    }
                }

                is LoadState.Error -> {
                    if (hasData) {
                        showSnackBarError()
                    } else {
                        viewModel.setLoadingState(LoadViewState.ERROR)
                    }
                    if (swipeRefresh.isRefreshing) {
                        viewModel.setLoadingState(LoadViewState.SWR_IS_NOT_VISIBLE)
                    }
                }

                is LoadState.NotLoading -> {
                    if (swipeRefresh.isRefreshing) {
                        viewModel.setLoadingState(LoadViewState.SWR_IS_NOT_VISIBLE)
                    }
                    if (viewModel.currentState.value != LoadViewState.NONE) {
                        if (hasData) {
                            viewModel.setLoadingState(LoadViewState.NONE)
                        } else {
                            viewModel.setLoadingState(LoadViewState.NOTHING_FOUND)
                        }
                    }
                }
            }
        }
    }

    private fun clearFilter() = with(binding) {
        filterEt.setText(EMPTY_STRING)
        filterEt.clearFocus()
        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() = with(binding) {
        val imm =
            container.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(container.windowToken, 0)
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.currentState.collect { currentState ->
                updateStatePresentation(currentState)
            }
        }

        lifecycleScope.launch {
            viewModel.movies.collectLatest {
                movieAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.query.debounce(UiConstants.TIMEOUT_FILTER).distinctUntilChanged().collect {
                viewModel.refreshData()
            }
        }
    }

    private fun setRecyclerView() {
        movieAdapter = MovieAdapter(
            onClickListener = { clickOnMovie(movie = it) },
            changeFavouriteStatus = { viewModel.changeFavouriteStatus(movieId = it) },
            getFavouriteStatusUseCase = viewModel.getFavouriteStatusUseCase
        )
        binding.moviesRv.adapter =
            movieAdapter.withLoadStateFooter(footer = MovieLoadStateAdapter { movieAdapter.retry() })
    }

    private fun showSnackBarError() {
        val snackBar = Snackbar.make(
            binding.container, R.string.error_message_snack, Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

    private fun clickOnMovie(movie: Movie) {
        val bundle = Bundle().apply {
            putSerializable(KEY_MOVIE_BUNDLE, movie)
        }
        val detailFragment = MovieDetailFragment()
        detailFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(com.example.androidjetpack.presentation.R.id.fragmentContainer, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showThemeSelectionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.title_theme_dialog))
        val themes = arrayOf(getString(R.string.light_theme), getString(R.string.dark_theme))
        builder.setItems(themes) { _, which ->
            when (which) {
                0 -> setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
                1 -> setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setAppTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        viewModel.setTheme(mode)
        requireActivity().recreate()
    }

    private fun updateStatePresentation(currentState: LoadViewState) = with(binding) {
        when (currentState) {
            LoadViewState.NONE -> {
                loadStatePresentation?.hideState()
            }

            LoadViewState.SWR_IS_NOT_VISIBLE -> {
                swipeRefresh.isRefreshing = false
            }

            LoadViewState.TRANSPARENT_LOADING -> {
                loadStatePresentation = TransparentLoadingStatePresentation(placeHolder)
                loadStatePresentation?.showState()
            }

            LoadViewState.MAIN_LOADING -> {
                loadStatePresentation = MainLoadingStatePresentation(placeHolder)
                loadStatePresentation?.showState()
            }

            LoadViewState.NOTHING_FOUND -> {
                loadStatePresentation = NotFoundStatePresentation(
                    placeHolder, getString(R.string.nothing_found_message, filterEt.text)
                )
                loadStatePresentation?.showState()
            }

            LoadViewState.ERROR -> {
                loadStatePresentation =
                    ErrorStatePresentation(placeHolder) { viewModel.refreshData() }
                loadStatePresentation?.showState()
            }
        }
    }
}