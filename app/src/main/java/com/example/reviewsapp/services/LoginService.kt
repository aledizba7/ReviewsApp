package com.example.reviewsapp.services

import com.example.reviewsapp.dtos.AuthDto
import com.example.reviewsapp.dtos.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login/")
    suspend fun login(@Body loginService: AuthDto) : Response<AuthResponse>

    @POST("user/")
    suspend fun registerUser(@Body authDto: AuthDto) : Response<AuthResponse>
}