package com.example.aplikasi_rumah_sakit_rawat_jalan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aplikasi_rumah_sakit_rawat_jalan.MainActivity
import com.example.aplikasi_rumah_sakit_rawat_jalan.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Tombol Poli Gigi
        binding.cardPoliGigi.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(PoliGigiFragment())
        }

        // Tombol Poli Mata
        binding.cardPoliMata.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(PoliMataFragment())
        }

        // Tombol Antrian Saya
        binding.cardAntrianSaya.setOnClickListener {
            (activity as? MainActivity)?.navigateToFragment(AppointmentFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}