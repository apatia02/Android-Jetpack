package com.example.androidjetpack.domain.entity

/**
 * Доменная модель списка жанров фильмов
 * @property genres Список жанров
 */
data class GenreList(
    val genres: List<Genre> = emptyList(),
)