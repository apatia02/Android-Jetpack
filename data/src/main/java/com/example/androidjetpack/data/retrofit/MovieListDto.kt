package com.example.androidjetpack.data.retrofit

import com.google.gson.annotations.SerializedName


data class MovieListDto (
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val listMovie: List<MovieDto>? = null,
    @SerializedName("total_pages") val totalPage: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null,
)