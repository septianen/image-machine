package com.septianen.imagemachine.view

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.septianen.imagemachine.R
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openQrScanner()
            } else {
                Toast.makeText(requireContext(), Message.REQUIRE_PERMISSION, Toast.LENGTH_SHORT).show()
            }
        }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvCodeReader.setOnClickListener {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        binding.cvMachineData.setOnClickListener {
            findNavController().navigate(R.id.openMachineList)
        }
    }

    private fun openQrScanner() {

        findNavController().navigate(R.id.openScanner)
    }
}