package com.example.androidjetpack.domain.use_case

import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.MovieList
import com.example.androidjetpack.domain.repository.FavoriteMoviesRepository
import com.example.androidjetpack.domain.repository.MovieRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/**
 * UseCase, который получает список фильмов.
 */
class GetMoviesUseCase @Inject constructor(
    private val moviesRepository: MovieRepository,
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {

    /**
     * Запрос на получение фильмов по фильтру, если фильтр пустой возвращает все фильмы
     */
    suspend operator fun invoke(query: String, page: Int): MovieList {
        val listMovie = if (query == EMPTY_STRING) {
            moviesRepository.getMovies(page)
        } else {
            moviesRepository.searchMovies(query, page)
        }
        return listMovie.setFavoriteStatusAndRussianDate()
    }

    /**
     * Установка у фильмов статуса избранности
     * Установка русской даты для фильмов
     */
    private suspend fun MovieList.setFavoriteStatusAndRussianDate(): MovieList =
        this.copy(listMovie = this.listMovie.map { movie ->
            movie.copy(
                isFavourite = favoriteMoviesRepository.isMovieFavorite(movie.id),
                releaseDate = movie.releaseDate.formatRussianDate()
            )
        })

    private fun String.formatRussianDate(): String {
        return runCatching {
            val inputFormatter = DateTimeFormatter.ofPattern(ENGLISH_DATE_PATTERN, Locale.ENGLISH)
            val date = LocalDate.parse(this, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern(RUSSIAN_DATE_PATTERN, Locale(RUSSIAN))
            date.format(outputFormatter)
        }.getOrDefault(EMPTY_STRING)
    }

    private companion object {
        const val ENGLISH_DATE_PATTERN = "yyyy-MM-dd"
        const val RUSSIAN_DATE_PATTERN = "d MMMM yyyy"
        const val RUSSIAN = "ru"
    }
}