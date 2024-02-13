package com.example.androidjetpack.data.movie

import com.example.androidjetpack.data.network.ServerConstants.FILTERED_MOVIE_URL
import com.example.androidjetpack.data.network.ServerConstants.MOVIE_URL
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Api для запросов, связанных с фильмами
 */
internal interface MovieApi {

    @GET(MOVIE_URL)
    suspend fun getMovies(): MovieListDto

    @GET(FILTERED_MOVIE_URL)
    suspend fun searchMovies(@Query("query") query: String): MovieListDto
}