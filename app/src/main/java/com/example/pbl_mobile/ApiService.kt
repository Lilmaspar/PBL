package com.example.pbl_mobile

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("api/kelompok_5/login.php")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/kelompok_5/register.php")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("api/kelompok_5/get_battery_data.php")
    fun getBateraiData(): Call<BateraiData>

    @PUT("api/kelompok_5/update.php") // Endpoint sesuai dengan file PHP
    fun updateProfil(@Body user: User): Call<UpdateResponse>
}


