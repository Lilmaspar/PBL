package com.example.pbl_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard : AppCompatActivity() {

    private val client = OkHttpClient()
    private val blynkToken = "9YNGpNtGHGxiorOqwRXd5oHzWAiTbORq" // Blynk token
    private val blynkUrl = "https://blynk.cloud/external/api/update"

    private lateinit var powerToggleButton: ToggleButton
    private lateinit var tvBatteryPercentage: TextView
    private lateinit var imgBatteryStatus: ImageView

    private var isOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        powerToggleButton = findViewById(R.id.power)
        tvBatteryPercentage = findViewById(R.id.tvBatteryPercentage)
        imgBatteryStatus = findViewById(R.id.imgBatteryStatus)
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)

        // Get user data from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", "User") ?: "User"
        tvGreeting.text = "Halo, $userName"

        val isServoOn = sharedPreferences.getBoolean("SERVO_STATE", false)
        powerToggleButton.isChecked = isServoOn

        // Servo Toggle Button
        powerToggleButton.setOnCheckedChangeListener { _, isChecked ->
            saveServoState(isChecked)
            sendServoCommand(isChecked)
        }

        // Fetch battery data
        fetchBatteryData()

        // Set up navigation buttons
        setupNavigationButtons()
    }

    // Save ToggleButton state to SharedPreferences
    private fun saveServoState(isChecked: Boolean) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean("SERVO_STATE", isChecked)
            .apply()
    }

    private fun power() {
        // Tentukan nilai berdasarkan status saat ini
        val value = if (isOn) "0" else "1"

        // Kirimkan nilai kecepatan dan arah ke ESP32
        val url = blynkUrl.toHttpUrlOrNull()?.newBuilder()
            ?.addQueryParameter("token", blynkToken)
            ?.addQueryParameter("pin", "V1")
            ?.addQueryParameter("value", value)
            ?.build()

        val request = url?.let { Request.Builder().url(it).build() }

        Thread {
            try {
                val response = request?.let { client.newCall(it).execute() }
                val responseBody = response?.body?.string()

                println("Response: $responseBody")

                runOnUiThread {
                    Toast.makeText(this, "Servo Bergerak", Toast.LENGTH_SHORT).show();
                }

                // Ubah status setelah pengiriman
                isOn = !isOn // Toggle status

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Gagal menghubungi Blink", Toast.LENGTH_SHORT).show();
                }
            }
        }.start()
    }

    private fun fetchBatteryData() {
        // Gunakan coroutine untuk memanggil fungsi suspend
        lifecycleScope.launch {
            try {
                // Mengambil data baterai menggunakan Retrofit API secara asynchronous
                val response = RetrofitClient.apiService.getBateraiData()

                // Memeriksa apakah respons berhasil
                if (response.isSuccessful) {
                    val data = response.body()

                    if (data != null) {
                        val batteryPercentage = calculateBatteryPercentage(data.current)
                        tvBatteryPercentage.text = "$batteryPercentage%"
                        imgBatteryStatus.setImageResource(
                            when {
                                batteryPercentage >= 80 -> R.drawable.baterai_full
                                batteryPercentage >= 50 -> R.drawable.baterai_half
                                batteryPercentage >= 20 -> R.drawable.baterai_low
                                else -> R.drawable.baterai_empty
                            }
                        )
                    }
                } else {
                    Toast.makeText(this@Dashboard, "Gagal mengambil data baterai", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Dashboard, "Kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupNavigationButtons() {
        findViewById<ImageView>(R.id.profil).setOnClickListener {
            startActivity(Intent(this, Profil::class.java))
        }
        findViewById<ImageView>(R.id.statusbaterai).setOnClickListener {
            startActivity(Intent(this, Baterai::class.java))
        }
        findViewById<ImageView>(R.id.setting).setOnClickListener {
            startActivity(Intent(this, Pengaturan::class.java))
        }
        findViewById<ImageView>(R.id.laporan).setOnClickListener {
            startActivity(Intent(this, Laporan::class.java))
        }
        findViewById<Button>(R.id.about).setOnClickListener {
            startActivity(Intent(this, About::class.java))
        }
    }

    private fun sendServoCommand(turnOn: Boolean) {
        val value = if (turnOn) "1" else "0" // "0" = on, "1" = off
        val url = blynkUrl.toHttpUrlOrNull()?.newBuilder()
            ?.addQueryParameter("token", blynkToken)
            ?.addQueryParameter("pin", "V1")
            ?.addQueryParameter("value", value)
            ?.build()

        if (url == null) {
            Toast.makeText(this, "URL tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        val request = Request.Builder().url(url).build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("Servo Response: $responseBody")
                val message = if (turnOn) "Motor Servo Nyala" else "Motor Servo Mati"
                runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Gagal menghubungi server Blynk", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun calculateBatteryPercentage(current: Float): Int {
        return when {
            current >= 9.0 -> 100
            current >= 6.1 -> 80
            current >= 5.1 -> 60
            current >= 3.1 -> 40
            current >= 2.0 -> 20
            else -> 10
        }
    }
}
