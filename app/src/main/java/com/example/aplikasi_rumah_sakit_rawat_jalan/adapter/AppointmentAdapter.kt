package com.example.aplikasi_rumah_sakit_rawat_jalan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_rumah_sakit_rawat_jalan.R
import com.example.aplikasi_rumah_sakit_rawat_jalan.model.Appointment
import com.example.aplikasi_rumah_sakit_rawat_jalan.model.StatusAppointment
import java.text.SimpleDateFormat
import java.util.*

class AppointmentAdapter(
    private val appointments: List<Appointment>,
    private val onActionClick: (Appointment, String) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNomorAntrian: TextView = view.findViewById(R.id.textNomorAntrian)
        val textTanggalJam: TextView = view.findViewById(R.id.textTanggalJam)
        val textKeluhan: TextView = view.findViewById(R.id.textKeluhan)
        val textStatus: TextView = view.findViewById(R.id.textStatus)
        val textEstimasiWaktu: TextView = view.findViewById(R.id.textEstimasiWaktu)
        val textSisaAntrian: TextView = view.findViewById(R.id.textSisaAntrian)
        val buttonDetail: Button = view.findViewById(R.id.buttonDetail)
        val buttonCancel: Button = view.findViewById(R.id.buttonCancel)
        val buttonRefresh: Button = view.findViewById(R.id.buttonRefresh)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))

        holder.textNomorAntrian.text = appointment.nomorAntrian.toString()
        holder.textTanggalJam.text = "${dateFormat.format(appointment.tanggalKunjungan)} - ${appointment.jamKunjungan}"
        holder.textKeluhan.text = appointment.keluhan

        when (appointment.status) {
            StatusAppointment.TERDAFTAR -> {
                holder.textStatus.text = "Terdaftar"
                holder.textStatus.setBackgroundResource(R.drawable.status_terdaftar_bg)
            }
            StatusAppointment.MENUNGGU -> {
                holder.textStatus.text = "Menunggu"
                holder.textStatus.setBackgroundResource(R.drawable.status_menunggu_bg)
            }
            StatusAppointment.SEDANG_DILAYANI -> {
                holder.textStatus.text = "Sedang Dilayani"
                holder.textStatus.setBackgroundResource(R.drawable.status_dipanggil_bg)
            }
            StatusAppointment.SELESAI -> {
                holder.textStatus.text = "Selesai"
                holder.textStatus.setBackgroundResource(R.drawable.status_selesai_bg)
            }
            StatusAppointment.DIBATALKAN -> {
                holder.textStatus.text = "Dibatalkan"
                holder.textStatus.setBackgroundResource(R.drawable.status_dibatalkan_bg)
            }
            StatusAppointment.TIDAK_HADIR -> {
                holder.textStatus.text = "Tidak Hadir"
                holder.textStatus.setBackgroundResource(R.drawable.status_dibatalkan_bg)
            }
        }

        if (appointment.status == StatusAppointment.MENUNGGU || appointment.status == StatusAppointment.TERDAFTAR) {
            val sisaAntrian = (1..5).random()
            val estimasiWaktu = sisaAntrian * 10

            holder.textSisaAntrian.text = "$sisaAntrian antrian lagi"
            holder.textEstimasiWaktu.text = "± $estimasiWaktu menit"

            holder.textSisaAntrian.visibility = View.VISIBLE
            holder.textEstimasiWaktu.visibility = View.VISIBLE
        } else {
            holder.textSisaAntrian.visibility = View.GONE
            holder.textEstimasiWaktu.visibility = View.GONE
        }

        holder.buttonDetail.setOnClickListener { onActionClick(appointment, "detail") }
        holder.buttonCancel.setOnClickListener { onActionClick(appointment, "cancel") }
        holder.buttonRefresh.setOnClickListener { onActionClick(appointment, "refresh") }

        if (appointment.status == StatusAppointment.SELESAI ||
            appointment.status == StatusAppointment.DIBATALKAN ||
            appointment.status == StatusAppointment.TIDAK_HADIR) {
            holder.buttonCancel.visibility = View.GONE
        } else {
            holder.buttonCancel.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = appointments.size
}