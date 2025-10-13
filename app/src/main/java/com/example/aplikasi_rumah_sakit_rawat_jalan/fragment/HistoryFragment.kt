package com.example.aplikasi_rumah_sakit_rawat_jalan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasi_rumah_sakit_rawat_jalan.R
import com.example.aplikasi_rumah_sakit_rawat_jalan.adapter.AppointmentAdapter
import com.example.aplikasi_rumah_sakit_rawat_jalan.databinding.FragmentHistoryBinding
import com.example.aplikasi_rumah_sakit_rawat_jalan.model.Appointment
import com.example.aplikasi_rumah_sakit_rawat_jalan.model.StatusAppointment
import java.util.*

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyAdapter: AppointmentAdapter
    private val historyList = mutableListOf<Appointment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadHistoryData()
    }

    private fun setupRecyclerView() {
        historyAdapter = AppointmentAdapter(historyList) { appointment, action ->
            // History adalah read-only, tidak ada action
        }

        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun loadHistoryData() {
        val calendar = Calendar.getInstance()

        historyList.clear()
        historyList.addAll(getHistoryAppointments(calendar))

        historyAdapter.notifyDataSetChanged()

        // Show/hide empty state
        if (historyList.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvHistory.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvHistory.visibility = View.VISIBLE
        }
    }

    private fun getHistoryAppointments(calendar: Calendar): List<Appointment> {
        // Data history (kunjungan yang sudah selesai)
        return listOf(
            Appointment(
                id = 101,
                pasienId = 1,
                dokterId = 3,
                poliklinikId = 1,
                tanggalKunjungan = calendar.apply { add(Calendar.DAY_OF_MONTH, -5) }.time,
                jamKunjungan = "14:00",
                keluhan = "Scaling dan pembersihan karang gigi",
                status = StatusAppointment.SELESAI,
                nomorAntrian = 8,
                tanggalDaftar = calendar.time,
                catatan = "Scaling selesai, kontrol 6 bulan lagi"
            ),
            Appointment(
                id = 102,
                pasienId = 1,
                dokterId = 1,
                poliklinikId = 1,
                tanggalKunjungan = calendar.apply { add(Calendar.DAY_OF_MONTH, -15) }.time,
                jamKunjungan = "09:00",
                keluhan = "Tambal gigi berlubang",
                status = StatusAppointment.SELESAI,
                nomorAntrian = 12,
                tanggalDaftar = calendar.time,
                catatan = "Tambal gigi geraham kiri berhasil"
            ),
            Appointment(
                id = 103,
                pasienId = 1,
                dokterId = 6,
                poliklinikId = 2,
                tanggalKunjungan = calendar.apply { add(Calendar.MONTH, -1) }.time,
                jamKunjungan = "10:00",
                keluhan = "Pemeriksaan mata rutin, cek minus",
                status = StatusAppointment.SELESAI,
                nomorAntrian = 5,
                tanggalDaftar = calendar.time,
                catatan = "Minus bertambah 0.5, resep kacamata baru"
            ),
            Appointment(
                id = 104,
                pasienId = 1,
                dokterId = 7,
                poliklinikId = 2,
                tanggalKunjungan = calendar.apply { add(Calendar.MONTH, -2) }.time,
                jamKunjungan = "13:00",
                keluhan = "Konsultasi katarak",
                status = StatusAppointment.SELESAI,
                nomorAntrian = 3,
                tanggalDaftar = calendar.time,
                catatan = "Katarak stadium awal, kontrol 3 bulan"
            ),
            Appointment(
                id = 105,
                pasienId = 1,
                dokterId = 4,
                poliklinikId = 1,
                tanggalKunjungan = calendar.apply { add(Calendar.MONTH, -3) }.time,
                jamKunjungan = "15:00",
                keluhan = "Cabut gigi bungsu",
                status = StatusAppointment.SELESAI,
                nomorAntrian = 7,
                tanggalDaftar = calendar.time,
                catatan = "Cabut gigi bungsu selesai, minum antibiotik"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}