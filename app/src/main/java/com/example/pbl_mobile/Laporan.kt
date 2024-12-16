package com.example.pbl_mobile

import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Laporan : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnBack: ImageButton
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        // Mengakses elemen UI secara manual menggunakan findViewById
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        btnBack = findViewById(R.id.btnBack)

        // Memanggil API untuk mendapatkan data laporan
        getDataLaporan()

        // Tombol back untuk kembali ke halaman sebelumnya
        btnBack.setOnClickListener {
            finish()
        }

        swipeRefreshLayout.setOnRefreshListener {
            getDataLaporan()
        }
    }

    private fun getDataLaporan() {
        swipeRefreshLayout.isRefreshing = true

        // Memanggil API menggunakan RetrofitClient
        RetrofitClient.apiService.getLaporanData().enqueue(object : Callback<List<ReportData>> {
            override fun onResponse(call: Call<List<ReportData>>, response: Response<List<ReportData>>) {
                swipeRefreshLayout.isRefreshing = false

                if (response.isSuccessful) {
                    val laporanData = response.body() ?: emptyList()

                    // Urutkan data berdasarkan waktu secara descending
                    val sortedList = laporanData.sortedByDescending { it.detected_at }.take(30)

                    // Ambil hanya 30 data terbaru
                    val limitedList = sortedList.take(30)

                    // Inisialisasi RecyclerView dengan data yang diambil
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



