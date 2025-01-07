package com.example.pbl_mobile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
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

        startClock()

        powerToggleButton = findViewById(R.id.power)
        tvBatteryPercentage = findViewById(R.id.tvBatteryPercentage)
        imgBatteryStatus = findViewById(R.id.imgBatteryStatus)
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)

        // Get user data from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", "User") ?: "User"
        tvGreeting.text = "Halo, $userName"

        // Servo Toggle Button
        powerToggleButton.setOnCheckedChangeListener { _, isChecked ->
            powerButton()
        }

        // Fetch battery data
        fetchBatteryData()

        // Set up navigation buttons
        setupNavigationButtons()
    }

    private fun startClock() {
        val handler = Handler(Looper.getMainLooper())
        val waktu = findViewById<TextView>(R.id.waktu)
        val tanggal = findViewById<TextView>(R.id.tanggal)

        val runnable = object : Runnable {
            override fun run() {
                // Format waktu dan tanggal
                val calendar = Calendar.getInstance()
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

                // Perbarui TextView
                waktu.text = timeFormat.format(calendar.time)
                tanggal.text = dateFormat.format(calendar.time)

                // Jalankan lagi setelah 1 detik
                handler.postDelayed(this, 1000)
            }
        }

        // Mulai Runnable
        handler.post(runnable)
    }

    private fun powerButton() {
        // Tentukan nilai berdasarkan status saat ini
        val value = if (isOn) "1" else "0"

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
                    // Check the value and display a different Toast message based on it
                    val toastMessage = if (value == "1") {
                        "Servo dinyalankan"
                    } else {
                        "Servo dimatikan"
                    }
                    Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
                }

                isOn = !isOn // Toggle status

                updateSettingButtonState()

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Gagal menghubungi Blink", Toast.LENGTH_SHORT).show();
                }
            }
        }.start()
    }

    private fun fetchBatteryData() {
        lifecycleScope.launch {
            val refreshInterval = 5000L // 5 detik
            while (true) {
                try {
                    val response = RetrofitClient.apiService.getBateraiData()

                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            val batteryPercentage = calculateBatteryPercentage(data.voltage)
                            withContext(Dispatchers.Main) {
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
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@Dashboard, "Gagal mengambil data baterai", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Dashboard, "Kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                delay(refreshInterval) // Tunggu interval sebelum refresh
            }
        }
    }

    private fun updateSettingButtonState() {
        val settingButton = findViewById<ImageView>(R.id.setting)
        val navSettings = findViewById<LinearLayout>(R.id.navSettings)
        val tvSettings = findViewById<TextView>(R.id.tvSettings)

        if (isOn) {
            settingButton.setImageResource(R.drawable.setting_icon)
            navSettings.setBackgroundResource(R.drawable.grey)
            tvSettings.setTextColor(ContextCompat.getColor(this, R.color.black))
            settingButton.isEnabled = false
        } else {
            settingButton.setImageResource(R.drawable.setting_icon)
            navSettings.setBackgroundResource(R.drawable.white)
            tvSettings.setTextColor(ContextCompat.getColor(this, R.color.matcha))
            settingButton.isEnabled = true
        }
    }

    private fun calculateBatteryPercentage(voltage: Float): Int {
        return when {
            voltage >= 9.0 -> 100
            voltage >= 6.1 -> 80
            voltage >= 5.1 -> 60
            voltage >= 3.1 -> 40
            voltage >= 2.0 -> 20
            else -> 10
        }
    }

    private fun setupNavigationButtons() {
        findViewById<ImageView>(R.id.profil).setOnClickListener {
            startActivity(Intent(this, Profil::class.java))
        }
        findViewById<ImageView>(R.id.statusbaterai).setOnClickListener {
            startActivity(Intent(this, Baterai::class.java))
        }
        val settingButton = findViewById<ImageView>(R.id.setting)
        settingButton.setOnClickListener {
            if (settingButton.isEnabled) {
                startActivity(Intent(this, Pengaturan::class.java))
            }
        }
        findViewById<ImageView>(R.id.laporan).setOnClickListener {
            startActivity(Intent(this, Laporan::class.java))
        }
        findViewById<Button>(R.id.about).setOnClickListener {
            startActivity(Intent(this, About::class.java))
        }
    }

}
