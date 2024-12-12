package com.example.pbl_mobile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager

// Kelas Onboarding yang merupakan bagian dari proses pengenalan aplikasi kepada pengguna baru
class Onboarding : AppCompatActivity() {
    // Deklarasi variabel untuk komponen UI dan SharedPreferences
    private lateinit var slideViewPager: ViewPager
    private lateinit var dotIndicator: LinearLayout
    private lateinit var backButton: Button
    private lateinit var nextButton: Button
    private lateinit var skipButton: Button
    private lateinit var dots: Array<TextView?>
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var sharedPreferences: SharedPreferences

    // Listener untuk perubahan halaman di ViewPager
    private var viewPagerListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            // Mengatur indikator titik saat halaman berubah
            setDotIndicator(position)

            // Menampilkan tombol "Back" hanya jika posisi halaman lebih dari 0
            backButton.visibility = if (position > 0) View.VISIBLE else View.INVISIBLE

            // Mengubah teks tombol "Next" menjadi "Finish" di halaman terakhir
            nextButton.text = if (position == 2) "Finish" else "Next"
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    // Metode yang dipanggil ketika aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi SharedPreferences untuk menyimpan status onboarding
        sharedPreferences = getSharedPreferences("OnBoardingPrefs", MODE_PRIVATE)
        val isFirstTime = sharedPreferences.getBoolean("isFirstTime", true)

        // Jika bukan pertama kali membuka aplikasi, langsung menuju halaman login
        if (isFirstTime) {
            startActivity(Intent(this@Onboarding, Login::class.java))
            finish()
            return  // Keluar dari metode onCreate agar tidak mengeksekusi lebih lanjut
        }

        // Mengatur layout yang digunakan oleh aktivitas ini
        setContentView(R.layout.activity_onboarding)

        // Inisialisasi komponen UI
        backButton = findViewById(R.id.backButton)
        nextButton = findViewById(R.id.nextButton)
        skipButton = findViewById(R.id.skipButton)
        slideViewPager = findViewById(R.id.slideViewPager)
        dotIndicator = findViewById(R.id.dotIndicator)

        // Inisialisasi ViewPager dengan adapter yang sesuai
        viewPagerAdapter = ViewPagerAdapter(this)
        slideViewPager.adapter = viewPagerAdapter

        // Set indikator titik di halaman pertama
        setDotIndicator(0)
        slideViewPager.addOnPageChangeListener(viewPagerListener)

        // Tombol "Back" untuk kembali ke halaman sebelumnya
        backButton.setOnClickListener {
            if (getItem(0) > 0) {
                slideViewPager.currentItem = getItem(-1)
            }
        }

        // Tombol "Next" untuk pindah ke halaman berikutnya, atau menyelesaikan onboarding
        nextButton.setOnClickListener {
            if (getItem(0) < 2) {
                slideViewPager.currentItem = getItem(1)
            } else {
                // Simpan status bahwa onboarding telah selesai
                sharedPreferences.edit().putBoolean("isFirstTime", false).apply()

                // Pindah ke halaman Mulai setelah onboarding selesai
                startActivity(Intent(this@Onboarding, Mulai::class.java))
                finish()
            }
        }

        // Tombol "Skip" untuk melewati onboarding dan langsung ke halaman login
        skipButton.setOnClickListener {
            // Simpan status bahwa onboarding telah dilewati
            sharedPreferences.edit().putBoolean("isFirstTime", false).apply()

            // Pindah ke halaman login
            startActivity(Intent(this@Onboarding, Login::class.java))
            finish()
        }
    }

    // Fungsi untuk mengatur indikator titik pada halaman onboarding
    private fun setDotIndicator(position: Int) {
        // Inisialisasi array TextView untuk titik indikator
        dots = arrayOfNulls(3)
        dotIndicator.removeAllViews()

        // Loop untuk membuat titik indikator sebanyak 3 buah
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY)
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(ResourcesCompat.getColor(resources, R.color.grey, applicationContext.theme))
            dotIndicator.addView(dots[i])
        }

        // Mengubah warna titik sesuai dengan halaman yang aktif
        dots[position]?.setTextColor(ResourcesCompat.getColor(resources, R.color.black, applicationContext.theme))
    }

    // Fungsi untuk mendapatkan posisi halaman saat ini di ViewPager
    private fun getItem(i: Int): Int {
        return slideViewPager.currentItem + i
    }
}