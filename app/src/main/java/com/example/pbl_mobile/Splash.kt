package com.example.pbl_mobile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

// Kelas Splash Screen yang mewarisi dari AppCompatActivity
class Splash : AppCompatActivity() {

    // Metode onCreate dipanggil saat aktivitas pertama kali dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  // Memanggil metode onCreate dari superclass (AppCompatActivity)
        setContentView(R.layout.activity_splash)  // Menentukan layout untuk aktivitas ini (activity_splash.xml)

        // Handler digunakan untuk menunda perpindahan ke aktivitas berikutnya
        Handler(Looper.getMainLooper()).postDelayed({
            // Intent untuk berpindah dari Splash ke Login
            val intent = Intent(this, Login::class.java)
            startActivity(intent)  // Memulai aktivitas login
            finish()  // Menutup aktivitas splash agar tidak bisa kembali ke layar splash saat tombol back ditekan
        }, 5000)  // Menunda selama 5 detik (5000 milidetik)
    }
}
