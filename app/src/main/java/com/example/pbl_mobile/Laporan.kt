package com.example.pbl_mobile

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Laporan : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        // Mengakses elemen UI secara manual menggunakan findViewById
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        btnBack = findViewById(R.id.btnBack)

        // Memanggil API untuk mendapatkan data laporan
        getDataLaporan()

        // Tombol back untuk kembali ke halaman sebelumnya
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getDataLaporan() {
        // Memanggil API menggunakan RetrofitClient
        RetrofitClient.apiService.getLaporanData().enqueue(object : Callback<List<ReportData>> {
            override fun onResponse(call: Call<List<ReportData>>, response: Response<List<ReportData>>) {
                if (response.isSuccessful) {
                    val laporanData = response.body() ?: emptyList()

                    // Urutkan data berdasarkan waktu secara descending
                    val sortedList = laporanData.sortedByDescending { it.detected_at }

                    // Inisialisasi RecyclerView dengan data yang diambil
                    recyclerView.layoutManager = LinearLayoutManager(this@Laporan)
                    recyclerView.adapter = LaporanAdapter(sortedList)
                } else {
                    Toast.makeText(this@Laporan, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReportData>>, t: Throwable) {
                Toast.makeText(this@Laporan, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}



