package com.example.reviewsapp.dtos

data class AuthResponse(
    val userId : Int,
    val isLogged : Boolean,
    val message : String
)
