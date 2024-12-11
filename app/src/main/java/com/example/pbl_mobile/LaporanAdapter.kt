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
    }

    override fun getItemCount(): Int = reportList.size
}

