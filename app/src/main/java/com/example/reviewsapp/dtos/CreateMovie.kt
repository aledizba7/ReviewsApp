package com.example.reviewsapp.dtos

data class CreateMovie (
    val title: String,
    val description: String,
    val year: Int,
    val genre: String,
    val rating: Float,
    val image_url: String,
)