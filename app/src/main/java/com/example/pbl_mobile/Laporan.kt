package com.example.pbl_mobile

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Laporan : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        // Mengakses elemen UI secara manual menggunakan findViewById
        recyclerView = findViewById(R.id.recyclerView)
        btnBack = findViewById(R.id.btnBack)

        // Data laporan (contoh)
        val reportList = listOf(
            ReportData("10.00", true, true),
            ReportData("09.30", false, false),
            ReportData("12.30", false, false),
            ReportData("14.20", true, true),
            ReportData("17.15", true, false),
            ReportData("08.45", true, false)

        )

        // Urutkan data secara descending berdasarkan waktu
        val sortedList = reportList.sortedByDescending { it.time }

        // Inisialisasi RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LaporanAdapter(sortedList)

        // Tombol back untuk kembali ke halaman sebelumnya
        btnBack.setOnClickListener {
            finish() // Mengakhiri aktivitas saat ini dan kembali
        }
    }
}
