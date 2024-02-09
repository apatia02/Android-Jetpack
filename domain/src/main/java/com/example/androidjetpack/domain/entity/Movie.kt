package com.example.androidjetpack.domain.entity

import com.example.androidjetpack.domain.DEFAULT_DOUBLE
import com.example.androidjetpack.domain.DEFAULT_INT
import com.example.androidjetpack.domain.EMPTY_STRING

/**
 * Доменная модель фильма
 */
data class Movie(
    val adult: Boolean = false,
    val backdropPath: String = EMPTY_STRING,
    val genreIds: List<Int> = emptyList(),
    val id: Int = DEFAULT_INT,
    val originalLanguage: String = EMPTY_STRING,
    val originalTitle: String = EMPTY_STRING,
    val description: String = EMPTY_STRING,
    val popularity: Double? = DEFAULT_DOUBLE,
    val posterPath: String = EMPTY_STRING,
    val releaseDate: String = EMPTY_STRING,
    val title: String = EMPTY_STRING,
    val video: Boolean = false,
    val voteAverage: Double = DEFAULT_DOUBLE,
    val voteCount: Int = DEFAULT_INT,
)