package com.example.androidjetpack.domain.repository

import com.example.androidjetpack.domain.entity.MovieList

/**
 * Интерфейс репозитория для запросов, связанных с фильмами
 */
interface MovieRepository {
    suspend fun getMovies(): MovieList

    suspend fun searchMovies(query: String): MovieList
}