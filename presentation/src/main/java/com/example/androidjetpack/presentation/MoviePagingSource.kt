package com.example.androidjetpack.presentation

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.domain.use_case.MoviesUseCase
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val moviesUseCase: MoviesUseCase,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: 1
            val movies = moviesUseCase.invoke(query, nextPage)
            LoadResult.Page(
                data = movies.listMovie,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (movies.listMovie.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val lastItem = state.lastItemOrNull()
        return lastItem?.id
    }
}