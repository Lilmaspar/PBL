package com.example.pbl_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Ambil TextView untuk menampilkan salam
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)

        // Mengambil data pengguna dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", "User") ?: "User"
        val userId = sharedPreferences.getString("USER_ID", "") ?: ""

        // Set teks untuk salam berdasarkan nama pengguna
        tvGreeting.text = "Hello, $userName"

        // Listener untuk tombol "Profil"
        val profilButton = findViewById<ImageView>(R.id.profil)
        profilButton.setOnClickListener {
            val intent = Intent(this, Profil::class.java)
            startActivity(intent)
        }

        // Listener untuk tombol "Status Baterai"
        val batteryStatusButton = findViewById<ImageView>(R.id.statusbaterai)
        batteryStatusButton.setOnClickListener {
            val intent = Intent(this, Baterai::class.java)
            startActivity(intent)
        }

        // Listener untuk tombol "Pengaturan"
        val settingsButton = findViewById<ImageView>(R.id.setting)
        settingsButton.setOnClickListener {
            val intent = Intent(this, Pengaturan::class.java)
            startActivity(intent)
        }

        // Listener untuk tombol "Laporan"
        val laporanButton = findViewById<ImageView>(R.id.laporan)
        laporanButton.setOnClickListener {
            val intent = Intent(this, Laporan::class.java)
            startActivity(intent)
        }

        // Listener untuk tombol "Tentang Aplikasi"
        val aboutButton = findViewById<Button>(R.id.about)
        aboutButton.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }
    }
}
