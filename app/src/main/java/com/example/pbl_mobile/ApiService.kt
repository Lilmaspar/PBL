package com.example.pbl_mobile

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @POST("api/kelompok_5/login.php")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("api/kelompok_5/register.php")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("api/kelompok_5/get_battery_data.php")
    suspend fun getBateraiData(): Response<BateraiData>

    @PUT("api/kelompok_5/update.php") // Endpoint sesuai dengan file PHP
    fun updateProfil(@Body user: User): Call<UpdateResponse>

    @GET("api/kelompok_5/get_laporan_data.php")
    fun getLaporanData(): Call<List<ReportData>>

    @FormUrlEncoded
    @POST("api/kelompok_5/save_history.php")
    fun saveReportData(
        @Field("keterangan") keterangan: String
    ): Call<ResponseBody>
}


