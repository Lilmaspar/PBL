package com.example.pbl_mobile

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/kelompok_5/login.php")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/kelompok_5/register.php")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
}


