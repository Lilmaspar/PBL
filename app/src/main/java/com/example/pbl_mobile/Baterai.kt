package com.example.pbl_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
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
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        // Hubungkan komponen layout dengan kode
        tvBatteryPercentage = findViewById(R.id.tvBatteryPercentage)
        tvBatteryVoltage = findViewById(R.id.tvBatteryVoltage)
        tvBatteryCurrent = findViewById(R.id.tvBatteryCurrent)
        tvBatteryPower = findViewById(R.id.tvBatteryPower)
        imgBatteryStatus = findViewById(R.id.imgBatteryStatus)

        // Mengambil data dari API menggunakan Retrofit
        RetrofitClient.apiService.getBateraiData().enqueue(object : Callback<BateraiData> {
            override fun onResponse(call: Call<BateraiData>, response: Response<BateraiData>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    if (data != null) {
                        // Mengatur data ke tampilan
                        val batteryPercentage = calculateBatteryPercentage(data.tegangan)
                        tvBatteryPercentage.text = "$batteryPercentage%"
                        tvBatteryVoltage.text = "Tegangan: ${data.tegangan}V"
                        tvBatteryCurrent.text = "Arus: ${data.arus}A"
                        tvBatteryPower.text = "Daya: ${data.daya}W"

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
                } else {
                    Toast.makeText(this@Baterai, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BateraiData>, t: Throwable) {
                Toast.makeText(this@Baterai, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun calculateBatteryPercentage(voltage: Float): Int {
        return when {
            voltage >= 13.5 -> 100
            voltage >= 13.0 -> 80
            voltage >= 12.5 -> 60
            voltage >= 12.0 -> 40
            voltage >= 11.5 -> 20
            else -> 10
        }
    }
}
