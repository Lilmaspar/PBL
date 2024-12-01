package com.example.pbl_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Tombol back untuk kembali ke halaman Dashboard
        findViewById<View>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish() // Menutup halaman About agar tidak kembali ke sini saat tombol back ditekan di Dashboard
        }
    }
}
