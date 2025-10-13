package com.example.aplikasi_rumah_sakit_rawat_jalan.model

import java.util.*

object AntrianManager {
    private val antrianList = mutableListOf<Appointment>()

    init {
        // Load dummy data awal
        loadDummyData()
    }

    private fun loadDummyData() {
        val calendar = Calendar.getInstance()

        antrianList.addAll(listOf(
            Appointment(
                id = 1,
                pasienId = 1,
                dokterId = 1,
                poliklinikId = 1,
                tanggalKunjungan = calendar.time,
                jamKunjungan = "09:00",
                keluhan = "Sakit gigi geraham kiri, nyeri sudah 2 hari",
                status = StatusAppointment.TERDAFTAR,
                nomorAntrian = 5,
                tanggalDaftar = calendar.time,
                catatan = "Bawa hasil rontgen gigi"
            ),
            Appointment(
                id = 2,
                pasienId = 1,
                dokterId = 6,
                poliklinikId = 2,
                tanggalKunjungan = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }.time,
                jamKunjungan = "10:00",
                keluhan = "Mata kabur saat membaca, periksa minus",
                status = StatusAppointment.MENUNGGU,
                nomorAntrian = 12,
                tanggalDaftar = calendar.time,
                catatan = "Kontrol mata rutin"
            )
        ))
    }

    fun getAllAntrian(): List<Appointment> {
        android.util.Log.d("AntrianManager", "Get all antrian: ${antrianList.size} items")
        return antrianList.sortedByDescending { it.id }
    }

    fun addAntrian(appointment: Appointment) {
        antrianList.add(0, appointment)
        android.util.Log.d("AntrianManager", "Antrian ditambahkan: ${appointment.nomorAntrian}, Total: ${antrianList.size}")
    }

    fun getNextId(): Int {
        return if (antrianList.isEmpty()) 1 else antrianList.maxOf { it.id } + 1
    }

    fun getNextNomorAntrian(): Int {
        return (antrianList.size + 1)
    }

    fun deleteAntrian(appointmentId: Int) {
        antrianList.removeAll { it.id == appointmentId }
    }
}