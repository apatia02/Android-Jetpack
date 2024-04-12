package com.example.androidjetpack.domain.use_case

import com.example.androidjetpack.domain.repository.FavoriteMoviesRepository
import javax.inject.Inject

/**
 * UseCase, который изменяет статус избранности у фильмов.
 */
class ChangeFavouriteStatusUseCase @Inject constructor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {

    /**
     * Запрос на получение фильмов по фильтру, если фильтр пустой возвращает все фильмы
     */
    suspend operator fun invoke(movieId: Int) {
        if (favoriteMoviesRepository.isMovieFavorite(movieId)) {
            favoriteMoviesRepository.removeFavoriteMovie(movieId)
        } else {
            favoriteMoviesRepository.addFavoriteMovie(movieId)
        }
    }
}