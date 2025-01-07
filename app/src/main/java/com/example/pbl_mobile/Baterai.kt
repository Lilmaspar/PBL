package com.example.pbl_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class Baterai : AppCompatActivity() {

    private lateinit var tvBatteryPercentage: TextView
    private lateinit var tvBatteryVoltage: TextView
    private lateinit var tvBatteryCurrent: TextView
    private lateinit var tvBatteryPower: TextView
    private lateinit var imgBatteryStatus: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baterai)

        findViewById<View>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Hubungkan komponen layout dengan kode
        tvBatteryPercentage = findViewById(R.id.tvBatteryPercentage)
        tvBatteryVoltage = findViewById(R.id.tvBatteryVoltage)
        tvBatteryCurrent = findViewById(R.id.tvBatteryCurrent)
        tvBatteryPower = findViewById(R.id.tvBatteryPower)
        imgBatteryStatus = findViewById(R.id.imgBatteryStatus)

        // Mengambil data dari API menggunakan Coroutine
        lifecycleScope.launch {
            // Panggil updateBatteryData pertama kali saat activity dimulai
            updateBatteryData()

            // Gunakan Coroutine untuk mengambil data secara berkala
            val refreshInterval = 5000L // interval dalam milidetik (5 detik)
            while (true) {
                updateBatteryData()  // Panggil updateBatteryData setiap interval
                delay(refreshInterval)  // Tunggu sesuai interval
            }
        }
    }

    // Fungsi untuk mengambil data baterai dan memperbarui UI
    private suspend fun updateBatteryData() {
        try {
            // Mengambil data dari API dengan Retrofit secara asinkron
            val response: Response<BateraiData> = RetrofitClient.apiService.getBateraiData()

            if (response.isSuccessful) {
                val data = response.body()

                if (data != null) {
                    // Mengatur data ke tampilan
                    val batteryPercentage = calculateBatteryPercentage(data.voltage)
                    withContext(Dispatchers.Main) {
                        tvBatteryPercentage.text = "$batteryPercentage%"
                        tvBatteryVoltage.text = "Tegangan: ${data.voltage} V"
                        tvBatteryCurrent.text = "Arus: ${data.current} mA"
                        tvBatteryPower.text = "Daya: ${data.power} mW"

                        // Ubah ikon status baterai sesuai kapasitas
                        imgBatteryStatus.setImageResource(
                            when {
                                batteryPercentage >= 80 -> R.drawable.baterai_full
                                batteryPercentage >= 50 -> R.drawable.baterai_half
                                batteryPercentage >= 20 -> R.drawable.baterai_low
                                else -> R.drawable.baterai_low
                            }
                        )
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    showToast("Gagal mengambil data")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                showToast("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    // Fungsi untuk menghitung persentase baterai
    private fun calculateBatteryPercentage(current: Float): Int {
        return when {
            current >= 9.0 -> 100
            current >= 6.1 -> 80
            current >= 5.1 -> 60
            current >= 3.1 -> 40
            current >= 2.0 -> 20
            else -> 10
        }
    }

    // Fungsi untuk menampilkan toast
    private fun showToast(message: String) {
        Toast.makeText(this@Baterai, message, Toast.LENGTH_SHORT).show()
    }
}
