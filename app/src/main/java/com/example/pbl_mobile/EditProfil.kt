package com.example.pbl_mobile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class EditProfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)
        // Tambahkan logika untuk mengedit data pengguna di sini

        // Tombol kembali ke Dashboard
        findViewById<View>(R.id.btnBack).setOnClickListener {
            finish() // Kembali ke Dashboard tanpa membuat instance baru
        }
    }

}
