package com.example.pbl_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Baterai : AppCompatActivity() {

    private lateinit var tvBatteryPercentage: TextView
    private lateinit var tvBatteryVoltage: TextView
    private lateinit var tvBatteryCurrent: TextView
    private lateinit var tvBatteryPower: TextView
    private lateinit var tvBatteryTemperature: TextView
    private lateinit var imgBatteryStatus: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baterai)

        findViewById<View>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish() // Menutup halaman About agar tidak kembali ke sini saat tombol back ditekan di Dashboard
        }

        // Hubungkan komponen layout dengan kode
        tvBatteryPercentage = findViewById(R.id.tvBatteryPercentage)
        tvBatteryVoltage = findViewById(R.id.tvBatteryVoltage)
        tvBatteryCurrent = findViewById(R.id.tvBatteryCurrent)
        tvBatteryPower = findViewById(R.id.tvBatteryPower)
        imgBatteryStatus = findViewById(R.id.imgBatteryStatus)

        // Contoh data, ini bisa berasal dari API atau sensor
        val batteryPercentage = 85 // Persentase kapasitas baterai
        val batteryVoltage = 12.5  // Tegangan baterai dalam Volt
        val batteryCurrent = 5.0   // Arus baterai dalam Ampere
        val batteryPower = batteryVoltage * batteryCurrent // Menghitung daya dalam Watt
        val batteryTemperature = 30 // Suhu baterai dalam Celcius

        // Atur data ke tampilan
        tvBatteryPercentage.text = "$batteryPercentage%"
        tvBatteryVoltage.text = "Tegangan: ${batteryVoltage}V"
        tvBatteryCurrent.text = "Arus: ${batteryCurrent}A"
        tvBatteryPower.text = "Daya: ${batteryPower}W"

        // Ubah ikon status baterai sesuai kapasitas (contoh perubahan sederhana)
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
