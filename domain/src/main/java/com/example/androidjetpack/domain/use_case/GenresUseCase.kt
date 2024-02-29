package com.example.androidjetpack.domain.use_case

import com.example.androidjetpack.domain.entity.GenreList
import com.example.androidjetpack.domain.repository.MovieRepository
import javax.inject.Inject

/**
 * UseCase, который по списку id жанров выдает сами жанры
 */
class GenresUseCase @Inject constructor(
    private val moviesRepository: MovieRepository
) {

    suspend fun getGenresFilteredWithId(listGenreId: List<Int>): GenreList {
        return moviesRepository.getGenres().filterWithListId(listGenreId)
    }

    private fun GenreList.filterWithListId(listGenreId: List<Int>): GenreList {
        return copy(genres = genres.filter { genre -> listGenreId.contains(genre.id) })
    }
}