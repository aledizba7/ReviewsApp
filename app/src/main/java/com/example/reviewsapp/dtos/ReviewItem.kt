package com.example.reviewsapp.dtos

data class ReviewItem(
    val content: String,
    val id: Int,
    val movie_id: Int,
    val rating: Double
) {
    val computedContent get() = if (content.length > 30)
        "${content.substring(0,30)}..." else content
}
