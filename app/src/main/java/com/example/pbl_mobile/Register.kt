package com.example.pbl_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etNomor: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etEmail = findViewById(R.id.etEmail)
        etNomor = findViewById(R.id.etNomor)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)

        // Handle tombol back untuk kembali ke halaman login
        findViewById<View>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Tutup halaman register agar tidak kembali ke sini dari halaman login
        }

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val email = etEmail.text.toString()
            val nomor = etNomor.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || nomor.isEmpty() || username.isEmpty() || password.isEmpty()) {
                // Validasi form
                return@setOnClickListener
            }

            val registerRequest = RegisterRequest(email, nomor, username, password)
            RetrofitClient.apiService.registerUser(registerRequest).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse?.status == "success") {
                            Toast.makeText(this@Register, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@Register, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@Register, registerResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@Register, "Registrasi gagal", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@Register, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}
