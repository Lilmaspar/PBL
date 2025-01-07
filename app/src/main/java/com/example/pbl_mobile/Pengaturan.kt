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
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    private lateinit var btnGerakkan: Button

    private var arah = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        // Back button
        findViewById<View>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // Initialize views
        tvSpeed = findViewById(R.id.tvSpeed)
        seekBarSpeed = findViewById(R.id.seekBarSpeed)
//      btnApplySettings = findViewById(R.id.btnApplySettings)
        tvStatusMessage = findViewById(R.id.tvStatusMessage)
        servoDirectionGroup = findViewById(R.id.servoDirectionGroup)
        servoDirectionTextView = findViewById(R.id.servoDirectionTextView)
        btnGerakkan = findViewById(R.id.btnGerakkan)

        loadSettings()

        // SeekBar untuk kecepatan
        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update TextView dengan nilai kecepatan berdasarkan progress
                val speedText = when (progress) {
                    0 -> "Kecepatan: Lambat"
                    1 -> "Kecepatan: Sedang"
                    2 -> "Kecepatan: Cepat"
                    else -> "Kecepatan: Sedang" // Default jika terjadi kesalahan
                }
                tvSpeed.text = speedText
                speedServo(progress)
                saveSettings()  // Simpan pengaturan ke SharedPreferences
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // RadioGroup untuk arah servo
        servoDirectionGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton = findViewById(checkedId)
            val directionValue = when (selectedRadioButton.id) {
                R.id.radio0Degree -> 0  // 0 derajat
                R.id.radio180Degree -> 1 // 180 derajat
                else -> 0 // Default jika tidak ada pilihan
            }

            // Update TextView dengan informasi arah yang dipilih
            val directionText = if (directionValue == 0) {
                "Arah Servo: 0°"
            } else {
                "Arah Servo: 180°"
            }

            servoDirectionTextView.text = directionText
            directionServo()
            saveSettings()  // Simpan pengaturan ke SharedPreferences
        }

        btnGerakkan.setOnClickListener {
            gerakkanServo()
        }
    }

    private fun directionServo() {
        // Tentukan nilai berdasarkan status saat ini
        val value = if (arah) "0" else "1"
//        val value = if (servoDirectionGroup.checkedRadioButtonId == R.id.radio0Degree) "0" else "1"

        // Kirimkan nilai kecepatan dan arah ke ESP32
        val url = blynkUrl.toHttpUrlOrNull()?.newBuilder()
            ?.addQueryParameter("token", blynkToken)
            ?.addQueryParameter("pin", "V3")
            ?.addQueryParameter("value", value)
            ?.build()

        val request = url?.let { Request.Builder().url(it).build() }

        Thread {
            try {
                val response = request?.let { client.newCall(it).execute() }
                val responseBody = response?.body?.string()

                println("Response: $responseBody")

                // Ubah status setelah pengiriman
                arah = !arah // Toggle status

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Gagal menghubungi Blynk", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun speedServo(speed: Int) {

        val value = when (speed) {
            0 -> "0" // Kecepatan rendah
            1 -> "1" // Kecepatan sedang
            2 -> "2" // Kecepatan cepat
            else -> "1" // Default kecepatan sedang jika tidak ada pilihan
        }

        val url = blynkUrl.toHttpUrlOrNull()?.newBuilder()
            ?.addQueryParameter("token", blynkToken)
            ?.addQueryParameter("pin", "V2")
            ?.addQueryParameter("value", value)
            ?.build()

        val request = url?.let { Request.Builder().url(it).build() }

        Thread {
            try {
                val response = request?.let { client.newCall(it).execute() }
                val responseBody = response?.body?.string()

                println("Response: $responseBody")

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Gagal menghubungi Blynk", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun gerakkanServo() {
        // Kirimkan nilai arah dan kecepatan yang telah disimpan
        val url = blynkUrl.toHttpUrlOrNull()?.newBuilder()
            ?.addQueryParameter("token", blynkToken)
            ?.addQueryParameter("pin", "V0")  // Pin untuk kecepatan
            ?.addQueryParameter("value", "1")  // Kecepatan servo
            ?.build()

        val request = url?.let { Request.Builder().url(it).build() }

        Thread {
            try {
                val response = request?.let { client.newCall(it).execute() }
                val responseBody = response?.body?.string()

                println("Response: $responseBody")

                runOnUiThread {
                    Toast.makeText(this, "Servo Bergerak", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Gagal menghubungi Blynk", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun saveSettings() {
        val sharedPreferences = getSharedPreferences("Pengaturan", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Simpan kecepatan (nilai SeekBar)
        editor.putInt("kecepatan", seekBarSpeed.progress)

        // Simpan arah servo (nilai RadioButton)
        val selectedRadioButtonId = servoDirectionGroup.checkedRadioButtonId
        editor.putInt("arah", selectedRadioButtonId)

        editor.apply()  // Simpan perubahan
    }

    private fun loadSettings() {
        val sharedPreferences = getSharedPreferences("Pengaturan", Context.MODE_PRIVATE)

        // Ambil nilai kecepatan dari SharedPreferences
        val speed = sharedPreferences.getInt("kecepatan", 1)  // Default 1 (Sedang)
        seekBarSpeed.progress = speed
        // Update TextView dengan nilai kecepatan berdasarkan progress
        tvSpeed.text = when (speed) {
            0 -> "Kecepatan: Lambat"
            1 -> "Kecepatan: Sedang"
            2 -> "Kecepatan: Cepat"
            else -> "Kecepatan: Sedang"
        }

        // Ambil nilai arah servo dari SharedPreferences
        val arah = sharedPreferences.getInt("arah", R.id.radio0Degree) // Default 0°
        servoDirectionGroup.check(arah)

        // Update TextView dengan informasi arah yang dipilih
        val directionText = if (arah == R.id.radio0Degree) "Arah Servo: 0°" else "Arah Servo: 180°"
        servoDirectionTextView.text = directionText
    }
}
