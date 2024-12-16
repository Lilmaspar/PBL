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


    private var directionValue = 0

    private var arah = false;

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
//      btnApplySettings = findViewById(R.id.btnApplySettings)
        tvStatusMessage = findViewById(R.id.tvStatusMessage)
        servoDirectionGroup = findViewById(R.id.servoDirectionGroup)
        servoDirectionTextView = findViewById(R.id.servoDirectionTextView)
        btnGerakkan = findViewById(R.id.btnGerakkan)


        // SeekBar untuk kecepatan
        seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update TextView dengan nilai kecepatan berdasarkan progress
                val speedText = when (progress) {
                    0 -> "Kecepatan: Lambat"
                    1 -> "Kecepatan: Sedang"
                    2 -> "Kecepatan: Cepat"
                    else -> "Kecepatan: Lambat" // Default jika terjadi kesalahan
                }
                tvSpeed.text = speedText

                // Kirim nilai kecepatan ke ESP32 menggunakan fungsi yang ada
                ishowspeed(progress)
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

            direction()
        }

        // Tombol apply settings
//        btnApplySettings.setOnClickListener {
//            applySettings()
//        }

        btnGerakkan.setOnClickListener {
            gerakkanServo()
        }
    }

/*    private fun applySettings() {
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
    }*/

    private fun direction() {
        // Tentukan nilai berdasarkan status saat ini
        val value = if (arah) "0" else "1"

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

                runOnUiThread {
                    Toast.makeText(this, "Motor Servo Berubah Arah", Toast.LENGTH_SHORT).show()
                }

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

    private fun ishowspeed(speed: Int) {

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

                runOnUiThread {
                    Toast.makeText(this, "Kecepatan motor servo berubah", Toast.LENGTH_SHORT).show()
                }

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

                // Simpan data ke database
                saveToDatabase("Servo dijalankan manual")

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

    private fun saveToDatabase(keterangan: String) {
        RetrofitClient.apiService.saveReportData(keterangan).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Pengaturan, "Data berhasil disimpan ke database", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Pengaturan, "Gagal menyimpan data: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Pengaturan, "Gagal menghubungi server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
