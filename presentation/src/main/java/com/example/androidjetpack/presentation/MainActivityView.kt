package com.example.androidjetpack.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidjetpack.base_resources.R.string
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.presentation.adapter.MovieAdapter
import com.example.androidjetpack.presentation.adapter.MovieLoadStateAdapter
import com.example.androidjetpack.presentation.databinding.ActivityMainViewBinding
import com.example.androidjetpack.presentation.extensions.insetKeyBoard
import com.example.androidjetpack.presentation.extensions.insetStatusBar
import com.example.androidjetpack.presentation.loading_state.ErrorStatePresentation
import com.example.androidjetpack.presentation.loading_state.LoadStatePresentation
import com.example.androidjetpack.presentation.loading_state.LoadViewState
import com.example.androidjetpack.presentation.loading_state.LoadViewState.ERROR
import com.example.androidjetpack.presentation.loading_state.LoadViewState.MAIN_LOADING
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NONE
import com.example.androidjetpack.presentation.loading_state.LoadViewState.NOTHING_FOUND
import com.example.androidjetpack.presentation.loading_state.LoadViewState.TRANSPARENT_LOADING
import com.example.androidjetpack.presentation.loading_state.MainLoadingStatePresentation
import com.example.androidjetpack.presentation.loading_state.NotFoundStatePresentation
import com.example.androidjetpack.presentation.loading_state.TransparentLoadingStatePresentation
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivityView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding

    private var loadStatePresentation: LoadStatePresentation? = null

    private val viewModel: MainViewModel by viewModels()

    private val movieAdapter =
        MovieAdapter(
            onClickListener = { showSnackBar(it) },
            changeFavouriteStatus = { viewModel.changeFavouriteStatus(movieId = it) }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onConfigurationChanged(newConfig: Configuration) = with(binding) {
        super.onConfigurationChanged(newConfig)
        saveScrollPosition()
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            moviesRv.layoutManager = GridLayoutManager(this@MainActivityView, 2)
        } else {
            moviesRv.layoutManager = LinearLayoutManager(this@MainActivityView)
        }
        moviesRv.scrollToPosition(viewModel.scrollPosition)
    }

    private fun init() {
        setRecyclerView()
        setInsets()
        setObservers()
        setListeners()
        AppCompatDelegate.setDefaultNightMode(viewModel.getTheme())
        setBarsTransparent()
    }

    private fun setInsets() {
        binding.container.insetStatusBar()
        binding.placeHolder.insetKeyBoard()
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
            viewModel.hasData = movieAdapter.itemCount > 0
            viewModel.setNewQuery(newText.toString())
            clearFilterBtn.isGone = newText.toString().isEmpty()
        }
        clearFilterBtn.setOnClickListener { clearFilter() }
        swipeRefresh.setOnRefreshListener {
            viewModel.setSwrVisible()
            viewModel.refreshData()
        }
        addAdapterListener()
        settingsIv.setOnClickListener { showThemeSelectionDialog() }
    }

    private fun addAdapterListener() {
        movieAdapter.addLoadStateListener { loadState ->
            viewModel.hasData = movieAdapter.itemCount > 0
            viewModel.setAdapterState(loadState.refresh)
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
        setMovieObserves()
        setStateScreenObserves()
    }

    private fun setStateScreenObserves() {
        lifecycleScope.launch {
            viewModel.currentLoadState.collect { currentState ->
                updateStatePresentation(currentState)
            }
        }
        lifecycleScope.launch {
            viewModel.snackError.collectLatest {
                showSnackBar(getString(string.error_message_snack))
            }
        }
        lifecycleScope.launch {
            viewModel.isSwrVisible.collectLatest {
                binding.swipeRefresh.isRefreshing = it
            }
        }
    }

    private fun setMovieObserves() {
        lifecycleScope.launch {
            viewModel.movies.collectLatest {
                movieAdapter.submitData(it)
            }
        }
    }

    private fun setRecyclerView() {
        binding.moviesRv.adapter =
            movieAdapter.withLoadStateFooter(footer = MovieLoadStateAdapter { movieAdapter.retry() })
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding.container,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

    private fun showThemeSelectionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(string.title_theme_dialog))
        val themes = arrayOf(getString(string.light_theme), getString(string.dark_theme))
        builder.setItems(themes) { _, which ->
            when (which) {
                0 -> setAppTheme(AppCompatDelegate.MODE_NIGHT_NO)
                1 -> setAppTheme(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setBarsTransparent() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
    }

    private fun setAppTheme(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        viewModel.setTheme(mode)
        recreate()
    }

    private fun updateStatePresentation(currentState: LoadViewState) = with(binding) {
        when (currentState) {
            NONE -> {
                loadStatePresentation?.hideState()
            }

            TRANSPARENT_LOADING -> {
                loadStatePresentation = TransparentLoadingStatePresentation(placeHolder)
                loadStatePresentation?.showState()
            }

            MAIN_LOADING -> {
                loadStatePresentation = MainLoadingStatePresentation(placeHolder)
                loadStatePresentation?.showState()
            }

            NOTHING_FOUND -> {
                loadStatePresentation = NotFoundStatePresentation(
                    placeHolder, getString(string.nothing_found_message, filterEt.text)
                )
                loadStatePresentation?.showState()
            }

            ERROR -> {
                loadStatePresentation =
                    ErrorStatePresentation(placeHolder) { viewModel.refreshData() }
                loadStatePresentation?.showState()
            }
        }
    }
}