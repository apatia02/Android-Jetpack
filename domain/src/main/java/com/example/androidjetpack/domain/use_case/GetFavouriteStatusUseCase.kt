package com.example.androidjetpack.domain.use_case

import com.example.androidjetpack.domain.repository.FavoriteMoviesRepository
import javax.inject.Inject

/**
 * UseCase, который получает статус фильма
 */
class GetFavouriteStatusUseCase @Inject constructor(
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {

    /**
     * Запрос на получение статуса избранности фильма
     */
    suspend fun getFavouriteStatus(movieId: Int): Boolean =
        favoriteMoviesRepository.isMovieFavorite(movieId)

}