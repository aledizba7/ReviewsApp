package com.example.reviewsapp.services

import com.example.reviewsapp.dtos.Movies
import com.example.reviewsapp.dtos.MoviesItem
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("movies")
    suspend fun getMovies() : Movies

    @GET("movies/{id}")
    suspend fun getMovieByID(@Path("id") id:Int) : MoviesItem

}