package com.example.reviewsapp.dtos

data class MoviesItem(
    val description: String,
    val genre: String,
    val id: Int,
    val image_url: String,
    val rating: Double,
    val title: String,
    val year: Int
)