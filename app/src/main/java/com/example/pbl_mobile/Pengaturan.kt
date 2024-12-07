package com.example.pbl_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Pengaturan : AppCompatActivity() {

    private lateinit var tvSpeed: TextView
    private lateinit var seekBarSpeed: SeekBar
    private lateinit var btnApplySettings: Button
    private lateinit var tvStatusMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        findViewById<View>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish() // Menutup halaman About agar tidak kembali ke sini saat tombol back ditekan di Dashboard
        }

        tvSpeed = findViewById(R.id.tvSpeed)
        seekBarSpeed = findViewById(R.id.seekBarSpeed)
        btnApplySettings = findViewById(R.id.btnApplySettings)
        tvStatusMessage = findViewById(R.id.tvStatusMessage)

        // Mengatur listener untuk seekBarSpeed
        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvSpeed.text = "Kecepatan: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnApplySettings.setOnClickListener {
            applySettings()
        }
    }

    private fun applySettings() {
        val speed = seekBarSpeed.progress

        // Menampilkan pengaturan yang diterapkan
        tvStatusMessage.text = "Pengaturan diterapkan: Kecepatan: $speed"

        // Anda bisa menambahkan logika untuk mengatur motor servo di sini
        // Misalnya, mengirim pengaturan ke perangkat keras atau microcontroller
    }
}
