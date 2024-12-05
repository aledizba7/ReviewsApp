package com.example.reviewsapp.services

import com.example.reviewsapp.dtos.AuthDto
import com.example.reviewsapp.dtos.AuthResponse
import com.example.reviewsapp.dtos.CreateReview
import com.example.reviewsapp.dtos.ReviewItem
import com.example.reviewsapp.dtos.Reviews
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {
    @GET("reviews")
    suspend fun getReviews() : Reviews

    @GET("reviews/{id}")
    suspend fun getReviewByID(@Path("id") id:Int) : ReviewItem

    @POST("movies/{title}/reviews")
    suspend fun createReview(@Path("title") title:String, @Body createReview: CreateReview)
}