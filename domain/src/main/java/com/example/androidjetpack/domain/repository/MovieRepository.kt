package com.example.androidjetpack.domain.repository

import com.example.androidjetpack.domain.entity.MovieList

/**
 * Интерфейс репозитория для запросов, связанных с фильмами
 */
interface MovieRepository {
    suspend fun getMovies(page: Int): MovieList

    suspend fun searchMovies(query: String, page: Int): MovieList
}