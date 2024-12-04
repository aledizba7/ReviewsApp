package com.example.reviewsapp.services

import com.example.reviewsapp.dtos.User  // Asegúrate de importar tu clase User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("user/{id}")
    suspend fun getUserById(@Path("id") userId: Int): User
}
