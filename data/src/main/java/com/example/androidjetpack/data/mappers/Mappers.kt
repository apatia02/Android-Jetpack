package com.example.androidjetpack.data.mappers

import com.example.androidjetpack.data.movie.GenreDto
import com.example.androidjetpack.data.movie.GenreListDto
import com.example.androidjetpack.data.movie.MovieDto
import com.example.androidjetpack.data.movie.MovieListDto
import com.example.androidjetpack.domain.DEFAULT_DOUBLE
import com.example.androidjetpack.domain.DEFAULT_INT
import com.example.androidjetpack.domain.EMPTY_STRING
import com.example.androidjetpack.domain.entity.Genre
import com.example.androidjetpack.domain.entity.GenreList
import com.example.androidjetpack.domain.entity.Movie
import com.example.androidjetpack.domain.entity.MovieList

/**
 * Маппер для трансформации dto в доменную модель фильма
 */
internal fun MovieDto.transformInMovie(): Movie = Movie(
    adult = this.adult ?: false,
    backdropPath = this.backdropPath ?: EMPTY_STRING,
    genreIds = this.genreIds ?: emptyList(),
    id = this.id ?: DEFAULT_INT,
    originalLanguage = this.originalLanguage ?: EMPTY_STRING,
    originalTitle = this.originalTitle ?: EMPTY_STRING,
    description = this.overview ?: EMPTY_STRING,
    popularity = this.popularity ?: DEFAULT_DOUBLE,
    posterPath = this.posterPath ?: EMPTY_STRING,
    releaseDate = this.releaseDate ?: EMPTY_STRING,
    title = this.title ?: EMPTY_STRING,
    video = this.video ?: false,
    voteAverage = this.voteAverage ?: DEFAULT_DOUBLE,
    voteCount = this.voteCount ?: DEFAULT_INT
)

/**
 * Маппер для трансформации dto в доменную модель списка фильмов
 */
internal fun MovieListDto.transformInMovieList(): MovieList = MovieList(
    page = this.page ?: DEFAULT_INT,
    listMovie = this.listMovie?.map { it.transformInMovie() } ?: emptyList(),
    totalPage = this.totalPage ?: DEFAULT_INT,
    totalResults = this.totalResults ?: DEFAULT_INT
)

/**
 * Маппер для трансформации dto в доменную модель жанра
 */
internal fun GenreDto.transformInGenre(): Genre = Genre(
    id = id ?: DEFAULT_INT,
    name = name ?: EMPTY_STRING
)

/**
 * Маппер для трансформации dto в доменную модель списка жанров
 */
internal fun GenreListDto.transformInGenreList(): GenreList = GenreList(
    genres = genres?.map { it.transformInGenre() } ?: emptyList(),
)