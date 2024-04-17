package com.example.androidjetpack.data.movie

import com.google.gson.annotations.SerializedName

/**
 * Dto для запроов, свзанных с списками жанров фильмов
 * @property genres Список жанров
 */
internal data class GenreListDto(
    @SerializedName("genres") val genres: List<GenreDto>? = null,
)