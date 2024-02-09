package com.example.androidjetpack.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Api для запросов, связанных с фильмами
 */
interface MovieApi {

    @GET(MOVIE_URL)
    suspend fun getMovies(): MovieListDto

    @GET(FILTERED_MOVIE_URL)
    suspend fun searchMovies(@Query("query") query: String): MovieListDto
}