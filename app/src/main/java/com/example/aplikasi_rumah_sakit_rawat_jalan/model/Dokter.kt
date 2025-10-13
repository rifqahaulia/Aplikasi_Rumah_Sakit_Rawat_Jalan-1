package com.example.aplikasi_rumah_sakit_rawat_jalan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dokter(
    val id: Int,
    val nama: String,
    val spesialis: String,
    val poliklinik: String, // "Poli Gigi" atau "Poli Mata"
    val hari: List<String>, // ["Senin", "Rabu"]
    val jamPraktek: String, // "08:00 - 14:00"
    val foto: Int, // R.drawable.ic_doctor
    val status: String // "Tersedia" atau "Tidak Tersedia"
) : Parcelable