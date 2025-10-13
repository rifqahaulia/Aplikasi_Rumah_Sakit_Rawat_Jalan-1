package com.example.aplikasi_rumah_sakit_rawat_jalan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_rumah_sakit_rawat_jalan.R
import com.example.aplikasi_rumah_sakit_rawat_jalan.model.Dokter

class DokterAdapter(
    private val dokterList: List<Dokter>,
    private val onItemClick: (Dokter) -> Unit
) : RecyclerView.Adapter<DokterAdapter.DokterViewHolder>() {

    class DokterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgDokter: ImageView = view.findViewById(R.id.imgDokter)
        val tvNama: TextView = view.findViewById(R.id.tvNamaDokter)
        val tvSpesialis: TextView = view.findViewById(R.id.tvSpesialis)
        val tvJadwal: TextView = view.findViewById(R.id.tvJadwal)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DokterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dokter, parent, false)
        return DokterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DokterViewHolder, position: Int) {
        val dokter = dokterList[position]

        holder.imgDokter.setImageResource(dokter.foto)
        holder.tvNama.text = dokter.nama
        holder.tvSpesialis.text = dokter.spesialis
        holder.tvJadwal.text = "${dokter.hari.joinToString(", ")} • ${dokter.jamPraktek}"
        holder.tvStatus.text = dokter.status

        holder.itemView.setOnClickListener {
            onItemClick(dokter)
        }
    }

    override fun getItemCount() = dokterList.size
}