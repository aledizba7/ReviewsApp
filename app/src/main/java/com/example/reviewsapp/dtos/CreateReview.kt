package com.example.reviewsapp.dtos

data class CreateReview (
    val content: String,
    val movie_id: Int,
    val rating: Double
)