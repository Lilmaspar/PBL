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
        holder.tvTime.text = "Waktu: ${report.time}"
        holder.tvSensorStatus.text =
            "Status Sensor: ${if (report.isDetected) "Terdeteksi" else "Tidak Terdeteksi"}"
        holder.tvDeviceStatus.text =
            "Status Perangkat: ${if (report.isDeviceActive) "Aktif" else "Tidak Aktif"}"

        // Ubah warna latar belakang berdasarkan kondisi
        val backgroundColor = if (report.isDetected && report.isDeviceActive) {
            Color.parseColor("#A5D6A7") // Hijau
        } else {
            Color.parseColor("#EF9A9A") // Merah
        }
        holder.itemView.setBackgroundColor(backgroundColor)
    }

    override fun getItemCount() = reportList.size
}

// Data model untuk laporan
data class ReportData(
    val time: String,
    val isDetected: Boolean,
    val isDeviceActive: Boolean
)
