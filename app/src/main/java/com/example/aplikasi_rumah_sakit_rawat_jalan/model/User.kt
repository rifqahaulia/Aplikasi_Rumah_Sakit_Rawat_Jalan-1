package com.example.aplikasi_rumah_sakit_rawat_jalan.model

data class User(
    val uid: String = "",
    val email: String = "",
    val nama: String = "",
    val noTelepon: String = "",
    val role: String = "pasien", // "pasien" atau "dokter"
    val spesialis: String = "" // khusus untuk dokter (contoh: "Sp.KG" untuk gigi)
)