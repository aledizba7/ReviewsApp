package com.example.reviewsapp.services

import com.example.reviewsapp.dtos.ReviewItem
import com.example.reviewsapp.dtos.Reviews
import retrofit2.http.GET
import retrofit2.http.Path

interface ReviewService {
    @GET("reviews")
    suspend fun getReviews() : Reviews

    @GET("reviews/{id}")
    suspend fun getReviewByID(@Path("id") id:Int) : ReviewItem
}