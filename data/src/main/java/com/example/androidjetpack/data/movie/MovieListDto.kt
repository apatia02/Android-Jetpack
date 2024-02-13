package com.example.androidjetpack.data.movie

import com.google.gson.annotations.SerializedName

/**
 * Dto для списка фильмов
 * @property page Номер страницы результата.
 * @property listMovie Список объектов фильмов.
 * @property totalPage Общее количество страниц результатов.
 * @property totalResults Общее количество результатов.
 */
internal data class MovieListDto (
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val listMovie: List<MovieDto>? = null,
    @SerializedName("total_pages") val totalPage: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null,
)