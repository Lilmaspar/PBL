package com.example.pbl_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KeteranganStatus : AppCompatActivity() {

    private lateinit var tvBatteryStatus: TextView
    private lateinit var tvWeatherCondition: TextView
    private lateinit var tvStatusMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keterangan_status)

        tvBatteryStatus = findViewById(R.id.tvBatteryStatus)
        tvWeatherCondition = findViewById(R.id.tvWeatherCondition)
        tvStatusMessage = findViewById(R.id.tvStatusMessage)

        // Menampilkan status baterai dan kondisi cuaca
        updateStatus("Sedang", "Cerah")

        // Menangani klik tombol
        val btnActivate: Button = findViewById(R.id.btnActivate)
        btnActivate.setOnClickListener {
            activateEnergySavingMode()
        }

        findViewById<View>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish() // Menutup halaman About agar tidak kembali ke sini saat tombol back ditekan di Dashboard
        }
    }

    private fun updateStatus(batteryStatus: String, weatherCondition: String) {
        tvBatteryStatus.text = "Status Baterai: $batteryStatus"
        tvWeatherCondition.text = "Kondisi Cuaca: $weatherCondition"
    }

    private fun activateEnergySavingMode() {
        // Logika untuk mengaktifkan mode hemat energi
        tvStatusMessage.text = "Mode hemat energi aktif! Penggunaan daya dioptimalkan."
        // Anda bisa menambahkan lebih banyak logika di sini untuk menyesuaikan frekuensi operasional
    }
}
