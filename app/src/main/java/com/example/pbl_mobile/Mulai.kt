package com.example.pbl_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

// Kelas Mulai yang mewarisi dari AppCompatActivity
class Mulai : AppCompatActivity() {
    // Deklarasi variabel untuk tombol mulai
    private lateinit var startButton: Button

    // Metode onCreate dipanggil ketika aktivitas pertama kali dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengatur layout untuk aktivitas ini
        setContentView(R.layout.activity_mulai)

        // Inisialisasi button dari layout
        startButton = findViewById(R.id.startButton)

        // Menambahkan fungsi onClickListener ke tombol
        startButton.setOnClickListener {
            // Membuat intent untuk berpindah dari aktivitas Mulai ke aktivitas Login
            val intent = Intent(this@Mulai, Login::class.java)
            startActivity(intent)  // Memulai aktivitas Login

            finish()  // Menutup aktivitas saat ini (Mulai) sehingga tidak kembali ketika tombol "Back" ditekan
        }
    }
}
