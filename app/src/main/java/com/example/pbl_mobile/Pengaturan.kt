package com.example.pbl_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class Pengaturan : AppCompatActivity() {

    private val client = OkHttpClient()
    private val blynkToken = "9YNGpNtGHGxiorOqwRXd5oHzWAiTbORq"
    private val blynkUrl = "https://blynk.cloud/external/api/update"

    private lateinit var tvSpeed: TextView
    private lateinit var seekBarSpeed: SeekBar
    private lateinit var btnApplySettings: Button
    private lateinit var tvStatusMessage: TextView
    private lateinit var servoDirectionGroup: RadioGroup
    private lateinit var servoDirectionTextView: TextView

    private var directionValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        // Back button
        findViewById<View>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }

        // Initialize views
        tvSpeed = findViewById(R.id.tvSpeed)
        seekBarSpeed = findViewById(R.id.seekBarSpeed)
        btnApplySettings = findViewById(R.id.btnApplySettings)
        tvStatusMessage = findViewById(R.id.tvStatusMessage)
        servoDirectionGroup = findViewById(R.id.servoDirectionGroup)
        servoDirectionTextView = findViewById(R.id.servoDirectionTextView)

        // SeekBar untuk kecepatan
        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val speedText = when (progress) {
                    0 -> "Lambat"
                    1 -> "Sedang"
                    2 -> "Cepat"
                    else -> "Sedang"
                }
                tvSpeed.text = "Kecepatan: $speedText"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // RadioGroup untuk arah servo
        servoDirectionGroup.setOnCheckedChangeListener { _, checkedId ->
            directionValue = when (checkedId) {
                R.id.radio0Degree -> 0
                R.id.radio180Degree -> 1
                else -> 0
            }
            servoDirectionTextView.text = "Arah Servo: ${if (directionValue == 0) "0°" else "180°"}"
        }

        // Tombol apply settings
        btnApplySettings.setOnClickListener {
            applySettings()
        }
    }

    private fun applySettings() {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isServoOn = sharedPreferences.getBoolean("SERVO_STATE", false)

        if (!isServoOn) {
            Toast.makeText(this, "Motor Servo dimatikan. Aktifkan dari Dashboard.", Toast.LENGTH_SHORT).show()
            return
        }

        val speed = seekBarSpeed.progress

        // Kirim pengaturan ke Blynk
        sendBlynkCommand("V2", speed.toString()) // Kirim kecepatan ke pin V2
        sendBlynkCommand("V3", directionValue.toString()) // Kirim arah ke pin V3

        tvStatusMessage.text =
            "Pengaturan diterapkan: Kecepatan: ${if (speed == 0) "Lambat" else if (speed == 1) "Sedang" else "Cepat"}, Arah: ${if (directionValue == 0) "0°" else "180°"}"
    }

    private fun sendBlynkCommand(pin: String, value: String) {
        val url = blynkUrl.toHttpUrlOrNull()?.newBuilder()
            ?.addQueryParameter("token", blynkToken)
            ?.addQueryParameter("pin", pin)
            ?.addQueryParameter("value", value)
            ?.build()

        val request = url?.let { Request.Builder().url(it).build() }

        Thread {
            try {
                val response = request?.let { client.newCall(it).execute() }
                val responseBody = response?.body?.string()

                println("Response: $responseBody")
                runOnUiThread {
                    Toast.makeText(this, "Pengaturan berhasil diterapkan ke $pin", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Gagal menghubungi Blynk", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
