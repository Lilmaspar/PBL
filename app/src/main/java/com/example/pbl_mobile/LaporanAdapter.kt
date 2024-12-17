package com.example.pbl_mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

// Adapter untuk RecyclerView
class LaporanAdapter(private val reportList: List<ReportData>) :
    RecyclerView.Adapter<LaporanAdapter.ReportViewHolder>() {

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_laporan, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportList[position]
        holder.tvTime.text = report.detected_at
        holder.tvKeterangan.text = report.keterangan

        // Tentukan warna berdasarkan keterangan
        val backgroundDrawable = when (report.keterangan) {
            "Servo nyala" -> ContextCompat.getDrawable(holder.itemView.context, R.drawable.matcha)
            "Burung terdeteksi, Motor Servo bergerak!" -> ContextCompat.getDrawable(holder.itemView.context, R.drawable.matcha)
            "Servo Dijalankan Manual" -> ContextCompat.getDrawable(holder.itemView.context, R.drawable.yellow)
            "Servo dijalankan manual" -> ContextCompat.getDrawable(holder.itemView.context, R.drawable.yellow)
            "Servo mati" -> ContextCompat.getDrawable(holder.itemView.context, R.drawable.pink)
            else -> ContextCompat.getDrawable(holder.itemView.context, R.drawable.grey)
        }

        // Set warna latar belakang item
        holder.itemView.background = backgroundDrawable
    }

    override fun getItemCount(): Int = reportList.size
}
