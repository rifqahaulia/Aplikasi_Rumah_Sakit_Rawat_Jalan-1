package com.example.aplikasi_rumah_sakit_rawat_jalan.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasi_rumah_sakit_rawat_jalan.adapter.AppointmentAdapter
import com.example.aplikasi_rumah_sakit_rawat_jalan.databinding.FragmentAppointmentBinding
import com.example.aplikasi_rumah_sakit_rawat_jalan.model.Appointment
import com.example.aplikasi_rumah_sakit_rawat_jalan.model.AntrianManager
import com.example.aplikasi_rumah_sakit_rawat_jalan.model.StatusAppointment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class AppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var appointmentAdapter: AppointmentAdapter
    private val appointmentList = mutableListOf<Appointment>()

    private val notificationHandler = Handler(Looper.getMainLooper())
    private val notificationRunnable = Runnable {
        checkAndShowNotification()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadDummyData()

        notificationHandler.postDelayed(notificationRunnable, 2000)
    }

    override fun onResume() {
        super.onResume()
        loadDummyData()
    }

    private fun setupRecyclerView() {
        appointmentAdapter = AppointmentAdapter(appointmentList) { appointment, action ->
            handleAppointmentAction(appointment, action)
        }

        binding.rvAppointments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = appointmentAdapter
        }
    }

    private fun handleAppointmentAction(appointment: Appointment, action: String) {
        when (action) {
            "detail" -> showDetailDialog(appointment)
            "cancel" -> showCancelConfirmation(appointment)
            "refresh" -> refreshAppointmentStatus(appointment)
        }
    }

    // ✅ FITUR 1: DETAIL - Tampilkan detail lengkap appointment (VERSI RAPI)
    private fun showDetailDialog(appointment: Appointment) {
        val ctx = context ?: return

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val dayFormat = SimpleDateFormat("EEEE", Locale("id", "ID"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val statusText = when (appointment.status) {
            StatusAppointment.TERDAFTAR -> "Terdaftar"
            StatusAppointment.MENUNGGU -> "Menunggu"
            StatusAppointment.SEDANG_DILAYANI -> "Sedang Dilayani"
            StatusAppointment.SELESAI -> "Selesai"
            StatusAppointment.DIBATALKAN -> "Dibatalkan"
            StatusAppointment.TIDAK_HADIR -> "Tidak Hadir"
        }

        val message = buildString {
            appendLine("Nomor Antrian: ${appointment.nomorAntrian}")
            appendLine("Status: $statusText")
            appendLine()
            appendLine("📅 Jadwal Kunjungan:")
            appendLine("${dayFormat.format(appointment.tanggalKunjungan)}, ${dateFormat.format(appointment.tanggalKunjungan)}")
            appendLine("Jam: ${appointment.jamKunjungan}")
            appendLine()
            appendLine("💬 Keluhan:")
            appendLine(appointment.keluhan)

            if (appointment.catatan.isNotEmpty()) {
                appendLine()
                appendLine("📝 Catatan:")
                appendLine(appointment.catatan)
            }

            appendLine()
            appendLine("📆 Tanggal Daftar:")
            appendLine("${dayFormat.format(appointment.tanggalDaftar)}, ${dateFormat.format(appointment.tanggalDaftar)} ${timeFormat.format(appointment.tanggalDaftar)}")
            appendLine()
            appendLine("ID Appointment: ${appointment.id}")
        }

        AlertDialog.Builder(ctx)
            .setTitle("📋 DETAIL ANTRIAN")
            .setMessage(message)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Refresh Status") { dialog, _ ->
                dialog.dismiss()
                refreshAppointmentStatus(appointment)
            }
            .show()
    }

    // ✅ FITUR 2: REFRESH - Update status antrian dari Firestore
    private fun refreshAppointmentStatus(appointment: Appointment) {
        val ctx = context ?: return

        Toast.makeText(ctx, "Memperbarui status antrian...", Toast.LENGTH_SHORT).show()

        val db = Firebase.firestore

        // Query ke Firestore berdasarkan ID appointment
        db.collection("antrian")
            .whereEqualTo("id", appointment.id)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Jika tidak ada di Firestore, simulasi update status lokal
                    simulateStatusUpdate(appointment)
                } else {
                    // Update dari Firestore
                    val document = documents.documents[0]
                    val statusString = document.getString("status") ?: "TERDAFTAR"
                    val newStatus = try {
                        StatusAppointment.valueOf(statusString)
                    } catch (e: Exception) {
                        StatusAppointment.TERDAFTAR
                    }

                    // Update status di AntrianManager
                    AntrianManager.updateStatus(appointment.id, newStatus)

                    // Reload data
                    loadDummyData()

                    Toast.makeText(
                        ctx,
                        "✅ Status diperbarui: ${getStatusText(newStatus)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    ctx,
                    "❌ Gagal refresh: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

                // Fallback: simulasi update
                simulateStatusUpdate(appointment)
            }
    }

    // Simulasi update status (untuk testing tanpa Firestore)
    private fun simulateStatusUpdate(appointment: Appointment) {
        val ctx = context ?: return

        // Simulasi perubahan status
        val newStatus = when (appointment.status) {
            StatusAppointment.TERDAFTAR -> StatusAppointment.MENUNGGU
            StatusAppointment.MENUNGGU -> StatusAppointment.SEDANG_DILAYANI
            StatusAppointment.SEDANG_DILAYANI -> StatusAppointment.SELESAI
            else -> appointment.status
        }

        if (newStatus != appointment.status) {
            AntrianManager.updateStatus(appointment.id, newStatus)
            loadDummyData()

            Toast.makeText(
                ctx,
                "✅ Status diperbarui: ${getStatusText(newStatus)}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                ctx,
                "Status sudah final: ${getStatusText(newStatus)}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // ✅ FITUR 3: BATAL - Batalkan appointment dengan konfirmasi
    private fun showCancelConfirmation(appointment: Appointment) {
        val ctx = context ?: return

        AlertDialog.Builder(ctx)
            .setTitle("⚠️ Batalkan Antrian?")
            .setMessage(
                "Apakah Anda yakin ingin membatalkan antrian ini?\n\n" +
                        "Nomor Antrian: ${appointment.nomorAntrian}\n" +
                        "Tanggal: ${SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(appointment.tanggalKunjungan)}\n" +
                        "Jam: ${appointment.jamKunjungan}\n\n" +
                        "Tindakan ini tidak dapat dibatalkan."
            )
            .setPositiveButton("Ya, Batalkan") { dialog, _ ->
                dialog.dismiss()
                cancelAppointment(appointment)
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun cancelAppointment(appointment: Appointment) {
        val ctx = context ?: return

        Toast.makeText(ctx, "Membatalkan antrian...", Toast.LENGTH_SHORT).show()

        val db = Firebase.firestore

        // Update status di Firestore
        db.collection("antrian")
            .whereEqualTo("id", appointment.id)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val documentId = documents.documents[0].id

                    db.collection("antrian")
                        .document(documentId)
                        .update("status", StatusAppointment.DIBATALKAN.name)
                        .addOnSuccessListener {
                            // Update lokal
                            AntrianManager.updateStatus(appointment.id, StatusAppointment.DIBATALKAN)
                            loadDummyData()

                            Toast.makeText(
                                ctx,
                                "✅ Antrian berhasil dibatalkan",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                ctx,
                                "❌ Gagal membatalkan: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    // Jika tidak ada di Firestore, update lokal saja
                    AntrianManager.updateStatus(appointment.id, StatusAppointment.DIBATALKAN)
                    loadDummyData()

                    Toast.makeText(
                        ctx,
                        "✅ Antrian berhasil dibatalkan",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    ctx,
                    "❌ Gagal membatalkan: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getStatusText(status: StatusAppointment): String {
        return when (status) {
            StatusAppointment.TERDAFTAR -> "Terdaftar"
            StatusAppointment.MENUNGGU -> "Menunggu"
            StatusAppointment.SEDANG_DILAYANI -> "Sedang Dilayani"
            StatusAppointment.SELESAI -> "Selesai"
            StatusAppointment.DIBATALKAN -> "Dibatalkan"
            StatusAppointment.TIDAK_HADIR -> "Tidak Hadir"
        }
    }

    private fun loadDummyData() {
        appointmentList.clear()
        appointmentList.addAll(AntrianManager.getAllAntrian())

        appointmentAdapter.notifyDataSetChanged()

        if (appointmentList.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvAppointments.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvAppointments.visibility = View.VISIBLE
        }
    }

    private fun checkAndShowNotification() {
        if (!isAdded || context == null) {
            return
        }

        appointmentList.forEach { appointment ->
            if (appointment.status == StatusAppointment.MENUNGGU ||
                appointment.status == StatusAppointment.TERDAFTAR) {

                val sisaAntrian = (1..5).random()

                if (sisaAntrian <= 3) {
                    showAlertDialog(appointment, sisaAntrian)
                    return@forEach
                }
            }
        }
    }

    private fun showAlertDialog(appointment: Appointment, sisaAntrian: Int) {
        val ctx = context
        if (ctx == null || !isAdded) {
            return
        }

        val alertDialog = AlertDialog.Builder(ctx)
            .setTitle("⚠️ Segera Bersiap!")
            .setMessage(
                "Antrian Anda hampir tiba!\n\n" +
                        "Nomor Antrian: ${appointment.nomorAntrian}\n" +
                        "Sisa Antrian: $sisaAntrian orang\n" +
                        "Estimasi: ${sisaAntrian * 10} menit\n\n" +
                        "Mohon bersiap dan datang ke ruang tunggu."
            )
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Lihat Detail") { _, _ ->
                showDetailDialog(appointment)
            }
            .setCancelable(false)
            .create()

        if (isAdded && context != null) {
            alertDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        notificationHandler.removeCallbacks(notificationRunnable)
        _binding = null
    }
}
