package com.example.pbl_mobile

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter untuk RecyclerView
class LaporanAdapter(private val reportList: List<ReportData>) :
    RecyclerView.Adapter<LaporanAdapter.ReportViewHolder>() {

    // ViewHolder untuk menampilkan data di tiap item
    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvSensorStatus: TextView = itemView.findViewById(R.id.tvSensorStatus)
        val tvDeviceStatus: TextView = itemView.findViewById(R.id.tvDeviceStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_laporan, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportList[position]
        holder.tvTime.text = "${report.time}"
        holder.tvSensorStatus.text =
            "${if (report.isDetected) "Terdeteksi" else "Tidak Terdeteksi"}"
        holder.tvDeviceStatus.text =
            "${if (report.isDeviceActive) "Aktif" else "Tidak Aktif"}"

        // Ubah warna latar belakang berdasarkan kondisi
        val background = if (report.isDetected && report.isDeviceActive) {
            holder.itemView.context.getDrawable(R.drawable.matcha) // Warna hijau
        } else {
            holder.itemView.context.getDrawable(R.drawable.pink) // Warna merah
        }

        // Set warna latar belakang pada root view
        holder.itemView.setBackground(background)
    }

    override fun getItemCount() = reportList.size
}

// Data model untuk laporan
data class ReportData(
    val time: String,
    val isDetected: Boolean,
    val isDeviceActive: Boolean
)
