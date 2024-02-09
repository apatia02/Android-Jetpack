package com.example.androidjetpack.domain.entity

import com.example.androidjetpack.domain.DEFAULT_INT

/**
 * Доменная модель списка фильмов
 */
data class MovieList(
    val page: Int = DEFAULT_INT,
    val listMovie: List<Movie> = emptyList(),
    val totalPage: Int = DEFAULT_INT,
    val totalResults: Int = DEFAULT_INT,
)