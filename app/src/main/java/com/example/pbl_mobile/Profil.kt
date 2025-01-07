package com.example.pbl_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Profil : AppCompatActivity() {

    private lateinit var tvEmail: TextView
    private lateinit var tvNomor: TextView
    private lateinit var tvUsername: TextView
    private lateinit var btnEditProfil: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        // Inisialisasi TextView
        tvEmail = findViewById(R.id.tvEmail)
        tvNomor = findViewById(R.id.tvNomor)
        tvUsername = findViewById(R.id.tvUsername)

        // Inisialisasi Tombol
        btnEditProfil = findViewById(R.id.btnupdate)
        btnLogout = findViewById(R.id.btnlogout)

        // Mengambil data dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", "User") ?: "User"
        val email = sharedPreferences.getString("EMAIL", "Email tidak tersedia") ?: "Email tidak tersedia"
        val nomor = sharedPreferences.getString("NOMOR", "Nomor tidak tersedia") ?: "Nomor tidak tersedia"
        val userId = sharedPreferences.getString("USER_ID", "") ?: ""

        // Menampilkan data di TextView
        tvEmail.text = email
        tvNomor.text = nomor
        tvUsername.text = userName

        // Tombol kembali ke Dashboard
        findViewById<View>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Tombol Edit Profil
        btnEditProfil.setOnClickListener {
            val intent = Intent(this@Profil, EditProfil::class.java)
            startActivity(intent) // Pindah ke halaman Edit Profil
        }

        // Tombol Log Out
        btnLogout.setOnClickListener {
            // Hapus data dari SharedPreferences
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Pindah ke halaman Login
            val intent = Intent(this@Profil, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}