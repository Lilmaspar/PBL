package com.example.pbl_mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

// Adapter untuk ViewPager yang digunakan untuk menampilkan slide dalam onboarding
class ViewPagerAdapter(var context: Context) : PagerAdapter() {
    // Array yang menyimpan gambar untuk setiap slide
    var sliderAllImages: IntArray = intArrayOf(
        R.drawable.ob3,  // Gambar untuk slide 1
        R.drawable.ob4,  // Gambar untuk slide 2
        R.drawable.ob1,  // Gambar untuk slide 3
    )

    // Array yang menyimpan judul untuk setiap slide
    var sliderAllTitle: IntArray = intArrayOf(
        R.string.slide1,  // Judul untuk slide 1
        R.string.slide2,  // Judul untuk slide 2
        R.string.slide3,  // Judul untuk slide 3
    )

    // Array yang menyimpan deskripsi untuk setiap slide
    var sliderAllDesc: IntArray = intArrayOf(
        R.string.desc1,  // Deskripsi untuk slide 1
        R.string.desc2,  // Deskripsi untuk slide 2
        R.string.desc3,  // Deskripsi untuk slide 3
    )

    // Mengembalikan jumlah slide yang ada
    override fun getCount(): Int {
        return sliderAllTitle.size  // Ukuran array judul mewakili jumlah slide
    }

    // Memeriksa apakah view sesuai dengan objek yang dibuat oleh instantiateItem
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout  // Memastikan view adalah instance dari LinearLayout
    }

    // Membuat dan menginisialisasi setiap item/slide di ViewPager
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // Mendapatkan LayoutInflater untuk meng-inflate layout slide
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate layout slide dari XML
        val view: View = layoutInflater.inflate(R.layout.activity_slide, container, false)

        // Inisialisasi elemen-elemen dalam layout slide
        val sliderImage = view.findViewById<View>(R.id.sliderImage) as ImageView  // Gambar slide
        val sliderTitle = view.findViewById<View>(R.id.sliderTitle) as TextView  // Judul slide
        val sliderDesc = view.findViewById<View>(R.id.sliderDesc) as TextView  // Deskripsi slide

        // Mengatur gambar, judul, dan deskripsi sesuai dengan posisi slide saat ini
        sliderImage.setImageResource(sliderAllImages[position])
        sliderTitle.setText(sliderAllTitle[position])
        sliderDesc.setText(sliderAllDesc[position])

        // Menambahkan view yang sudah diinisialisasi ke ViewPager
        container.addView(view)

        return view  // Mengembalikan view yang telah diinisialisasi
    }

    // Menghapus item/slide dari ViewPager
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // Menghapus view dari container ViewPager
        container.removeView(`object` as LinearLayout)
    }
}
