package com.example.aplikasi_rumah_sakit_rawat_jalan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Appointment(
    val id: Int,
    val pasienId: Int,
    val dokterId: Int,
    val poliklinikId: Int,
    val tanggalKunjungan: Date,
    val jamKunjungan: String,
    val keluhan: String,
    val status: StatusAppointment,
    val nomorAntrian: Int,
    val tanggalDaftar: Date,
    val catatan: String = ""
) : Parcelable