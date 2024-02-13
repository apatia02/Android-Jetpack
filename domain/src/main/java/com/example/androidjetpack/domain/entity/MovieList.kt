package com.example.androidjetpack.domain.entity

import com.example.androidjetpack.domain.DEFAULT_INT

/**
 * Доменная модель списка фильмов
 * @property page Номер страницы результата.
 * @property listMovie Список объектов фильмов.
 * @property totalPage Общее количество страниц результатов.
 * @property totalResults Общее количество результатов.
 */
data class MovieList(
    val page: Int = DEFAULT_INT,
    val listMovie: List<Movie> = emptyList(),
    val totalPage: Int = DEFAULT_INT,
    val totalResults: Int = DEFAULT_INT,
)