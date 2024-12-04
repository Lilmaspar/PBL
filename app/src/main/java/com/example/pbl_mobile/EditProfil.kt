package com.example.pbl_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfil : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editNomor: EditText
    private lateinit var editUsername: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        editEmail = findViewById(R.id.editEmail)
        editNomor = findViewById(R.id.editNomor)
        editUsername = findViewById(R.id.editUsername)
        btnSave = findViewById(R.id.btnupdate)

        // Tombol kembali ke halaman Profil
        findViewById<View>(R.id.btnBack).setOnClickListener {
            finish() // Kembali ke Profil tanpa membuat instance baru
        }

        // Mengambil data dari SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", "User") ?: "User"
        val email = sharedPreferences.getString("EMAIL", "Email tidak tersedia") ?: "Email tidak tersedia"
        val nomor = sharedPreferences.getString("NOMOR", "Nomor tidak tersedia") ?: "Nomor tidak tersedia"
        val userId = sharedPreferences.getString("USER_ID", "") ?: ""

        // Menampilkan data di EditText
        editEmail.setText(email)
        editNomor.setText(nomor)
        editUsername.setText(userName)

        // Tombol Simpan
        btnSave.setOnClickListener {
            val updatedUsername = editUsername.text.toString()
            val updatedEmail = editEmail.text.toString()
            val updatedNomor = editNomor.text.toString()

            // Validasi input
            if (updatedEmail.isEmpty() || updatedNomor.isEmpty() || updatedUsername.isEmpty()) {
                Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mengupdate data ke database melalui Retrofit
            val user = User(userId, updatedUsername, updatedEmail, updatedNomor)

            RetrofitClient.apiService.updateProfil(user).enqueue(object : Callback<UpdateResponse> {
                override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                    if (response.isSuccessful) {
                        val updateResponse = response.body()
                        if (updateResponse?.status == "success") {
                            // Update SharedPreferences dengan data baru
                            val editor = sharedPreferences.edit()
                            editor.putString("USER_NAME", updatedUsername)
                            editor.putString("EMAIL", updatedEmail)
                            editor.putString("NOMOR", updatedNomor)
                            editor.apply()

                            // Kembali ke halaman Profil
                            val intent = Intent(this@EditProfil, Profil::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@EditProfil, updateResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@EditProfil, "Gagal memperbarui data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    Toast.makeText(this@EditProfil, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}