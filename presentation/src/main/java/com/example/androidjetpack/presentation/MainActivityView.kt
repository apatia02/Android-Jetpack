package com.example.androidjetpack.presentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.marginBottom
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.androidjetpack.base_resources.R.string
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.presentation.databinding.ActivityMainViewBinding
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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList

@AndroidEntryPoint
class MainActivityView : AppCompatActivity() {

    private lateinit var binding: ActivityMainViewBinding

    private var loadStatePresentation: LoadStatePresentation? = null

    private val viewModel: MainViewModel by viewModels()

    private val adapter = EasyAdapter()

    private val itemController = MovieItemController { showSnackBarMovie(title = it) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.container.insetKeyBoardMargin()
        setRecyclerView()
        setObservers()
        setListeners()
    }

    /**
     * метод, получающий inset клавиатуры и увеличивающий margin для view по этому inset
     */
    private fun View.insetKeyBoardMargin() {
        val initialMargin = this.marginBottom
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            val insetKeyBoard = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val insetNavBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val margin = if (insetKeyBoard == 0) {
                initialMargin
            } else {
                insetKeyBoard + initialMargin - insetNavBar
            }
            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                bottomMargin = margin
                view.layoutParams = this
            }
            insets
        }
    }

    private fun setListeners() = with(binding) {
        filterEt.addTextChangedListener { newText ->
            viewModel.setNewQuery(newText.toString())
            clearFilterBtn.isGone = newText.toString().isEmpty()
        }
        clearFilterBtn.setOnClickListener { clearFilter() }
        swipeRefresh.setOnRefreshListener { getMovies(isSwr = true) }
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
            viewModel.currentState.collect { currentState ->
                updateStatePresentation(currentState)
            }
        }
        lifecycleScope.launch {
            viewModel.snackError.collect {
                showSnackBarError()
            }
        }
    }

    private fun setMovieObserves() {
        lifecycleScope.launch {
            viewModel.query
                .debounce(UiConstants.TIMEOUT_FILTER)
                .distinctUntilChanged()
                .collect {
                    viewModel.getMovies(it)
                }
        }
        lifecycleScope.launch {
            viewModel.movies.collect { movies ->
                adapter.setItems(ItemList.create().addAll(movies.listMovie, itemController))
                binding.moviesRv.scrollToPosition(0)
            }
        }
    }

    private fun getMovies() {
        lifecycleScope.launch {
            viewModel.swrIsVisible.collect {
                swipeRefresh.isRefreshing = it
            }
        }
    }

    private fun getMovies(isSwr: Boolean = false) {
        lifecycleScope.launch {
            viewModel.getMovies(viewModel.query.value, isSwr)
            viewModel.getMovies()
        }
    }

    private fun setRecyclerView() {
        binding.moviesRv.adapter = adapter
    }

    private fun showSnackBarError() {
        val snackBar = Snackbar.make(
            binding.container,
            string.error_message_snack,
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

    private fun showSnackBarMovie(title: String) {
        val snackBar = Snackbar.make(
            binding.container,
            title,
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
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
                    ErrorStatePresentation(placeHolder) { getMovies() }
                loadStatePresentation?.showState()
            }
        }
    }
}