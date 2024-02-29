package com.example.androidjetpack.domain.entity

import com.example.androidjetpack.domain.DEFAULT_INT
import com.example.androidjetpack.domain.EMPTY_STRING

/**
 * Доменная модель жанра фильмов
 * @property id Уникальный идентификатор жанра
 * @property name Наименование жанра
 */
data class Genre(
    val id: Int = DEFAULT_INT,
    val name: String = EMPTY_STRING,
)