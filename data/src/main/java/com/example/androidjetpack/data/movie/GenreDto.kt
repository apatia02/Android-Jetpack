package com.example.androidjetpack.data.movie

import com.google.gson.annotations.SerializedName

/**
 * Dto для запроов, свзанных с жанрами фильмов
 * @property id Уникальный идентификатор жанра
 * @property name Наименование жанра
 */
internal data class GenreDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
)