package com.example.aplikasi_rumah_sakit_rawat_jalan.fragment

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

class AppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var appointmentAdapter: AppointmentAdapter
    private val appointmentList = mutableListOf<Appointment>()

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

        // Cek notifikasi setelah 2 detik (simulasi)
        Handler(Looper.getMainLooper()).postDelayed({
            checkAndShowNotification()
        }, 2000)
    }

    override fun onResume() {
        super.onResume()
        // Refresh data setiap kali fragment muncul
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
            "detail" -> {
                Toast.makeText(context, "Detail antrian #${appointment.nomorAntrian}", Toast.LENGTH_SHORT).show()
            }
            "cancel" -> {
                Toast.makeText(context, "Membatalkan antrian", Toast.LENGTH_SHORT).show()
            }
            "refresh" -> {
                Toast.makeText(context, "Refresh status antrian", Toast.LENGTH_SHORT).show()
                loadDummyData() // Refresh data
            }
        }
    }

    private fun loadDummyData() {
        appointmentList.clear()
        appointmentList.addAll(AntrianManager.getAllAntrian())

        appointmentAdapter.notifyDataSetChanged()

        // Show/hide empty state
        if (appointmentList.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvAppointments.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvAppointments.visibility = View.VISIBLE
        }
    }

    private fun checkAndShowNotification() {
        // Cek setiap antrian yang statusnya MENUNGGU atau TERDAFTAR
        appointmentList.forEach { appointment ->
            if (appointment.status == StatusAppointment.MENUNGGU ||
                appointment.status == StatusAppointment.TERDAFTAR) {

                // Simulasi: hitung sisa antrian (random 1-5)
                val sisaAntrian = (1..5).random()

                // Jika tinggal 3 atau kurang, tampilkan notifikasi
                if (sisaAntrian <= 3) {
                    showAlertDialog(appointment, sisaAntrian)
                    return@forEach // Hanya tampilkan 1 notifikasi
                }
            }
        }
    }

    private fun showAlertDialog(appointment: Appointment, sisaAntrian: Int) {
        val alertDialog = android.app.AlertDialog.Builder(requireContext())
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
                Toast.makeText(
                    context,
                    "Detail antrian #${appointment.nomorAntrian}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}