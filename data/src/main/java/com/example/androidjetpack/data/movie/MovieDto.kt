package com.example.androidjetpack.data.movie

import com.google.gson.annotations.SerializedName

/**
 * Dto для запроов, свзанных с фильмами
 * @property adult Показывает, подходит ли фильм для взрослой аудитории.
 * @property backdropPath Путь к изображению фона фильма.
 * @property genreIds Список идентификаторов жанров, связанных с фильмом.
 * @property id Уникальный идентификатор фильма.
 * @property originalLanguage Оригинальный язык фильма.
 * @property originalTitle Оригинальное название фильма.
 * @property overview Описание или краткое содержание фильма.
 * @property popularity Рейтинг популярности фильма.
 * @property posterPath Путь к постеру фильма.
 * @property releaseDate Дата выпуска фильма.
 * @property title Название фильма.
 * @property video Показывает, содержит ли фильм видеоконтент.
 * @property voteAverage Средний рейтинг фильма.
 * @property voteCount Количество голосов, полученных фильмом.
 */
internal data class MovieDto(
    @SerializedName("adult") val adult: Boolean? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("genre_ids") val genreIds: List<Int>? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("original_language") val originalLanguage: String? = null,
    @SerializedName("original_title") val originalTitle: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("popularity") val popularity: Double? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("video") val video: Boolean? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("vote_count") val voteCount: Int? = null,
)