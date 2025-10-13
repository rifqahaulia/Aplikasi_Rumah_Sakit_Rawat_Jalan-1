package com.example.aplikasi_rumah_sakit_rawat_jalan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_rumah_sakit_rawat_jalan.R
import com.example.aplikasi_rumah_sakit_rawat_jalan.model.Pendaftaran

class PasienDokterAdapter(
    private val listPasien: List<Pendaftaran>,
    private val onPeriksaClick: (Pendaftaran) -> Unit
) : RecyclerView.Adapter<PasienDokterAdapter.PasienViewHolder>() {

    inner class PasienViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNomorAntrian: TextView = itemView.findViewById(R.id.tv_nomor_antrian)
        val tvNamaPasien: TextView = itemView.findViewById(R.id.tv_nama_pasien)
        val tvNoTelepon: TextView = itemView.findViewById(R.id.tv_no_telepon)
        val tvKeluhan: TextView = itemView.findViewById(R.id.tv_keluhan)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val btnPeriksa: Button = itemView.findViewById(R.id.btn_periksa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasienViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pasien_dokter, parent, false)
        return PasienViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasienViewHolder, position: Int) {
        val pasien = listPasien[position]

        holder.tvNomorAntrian.text = pasien.nomorAntrian.toString()
        holder.tvNamaPasien.text = pasien.namaPasien
        holder.tvNoTelepon.text = pasien.noTelepon
        holder.tvKeluhan.text = pasien.keluhan

        // Set status
        when (pasien.status) {
            "menunggu" -> {
                holder.tvStatus.text = "Menunggu"
                holder.tvStatus.setBackgroundResource(R.drawable.status_menunggu_bg)
            }
            "sedang_diperiksa" -> {
                holder.tvStatus.text = "Sedang Diperiksa"
                holder.tvStatus.setBackgroundResource(R.drawable.status_menunggu_bg)
            }
            "selesai" -> {
                holder.tvStatus.text = "Selesai"
                holder.tvStatus.setBackgroundResource(R.drawable.status_menunggu_bg)
            }
        }

        // Tombol Periksa
        holder.btnPeriksa.setOnClickListener {
            onPeriksaClick(pasien)
        }
    }

    override fun getItemCount(): Int = listPasien.size
}