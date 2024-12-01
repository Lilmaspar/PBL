package com.example.pbl_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        tvRegister = findViewById(R.id.tvRegister)

        // Menangani klik pada tombol Login
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val usernameInput = etUsername.text.toString().trim()
            val passwordInput = etPassword.text.toString().trim()

            if (usernameInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                val loginRequest = LoginRequest(usernameInput, passwordInput)
                RetrofitClient.apiService.loginUser(loginRequest)
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            if (response.isSuccessful) {
                                val user = response.body()?.data // Ambil data user dari respons API
                                if (user != null) {
                                    // Simpan data ke SharedPreferences
                                    val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("USER_NAME", user.username)
                                    editor.putString("EMAIL", user.email)
                                    editor.putString("NOMOR", user.no_hp)
                                    editor.apply()

                                    // Arahkan ke Dashboard
                                    val intent = Intent(this@Login, Dashboard::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this@Login, "Login gagal: Data tidak valid", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@Login, "Login gagal: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(this@Login, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                if (usernameInput.isEmpty()) {
                    etUsername.error = "Username harus diisi"
                }
                if (passwordInput.isEmpty()) {
                    etPassword.error = "Password harus diisi"
                }
            }
        }

        // Menangani klik pada teks "Belum punya akun? Daftar di sini"
        tvRegister.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }
    }
}
