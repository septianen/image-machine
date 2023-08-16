package com.septianen.imagemachine.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.septianen.imagemachine.R
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.databinding.FragmentHomeBinding
import com.septianen.imagemachine.utils.Resource
import com.septianen.imagemachine.viewmodel.MachineListViewModel
import dagger.hilt.android.AndroidEntryPoint

class HomeFragment : Fragment() {

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
            showMessage("COming Soon")
        }

        binding.cvMachineData.setOnClickListener {
            findNavController().navigate(R.id.openMachineList)
        }
    }

    private fun showMessage(message: String?) {
        Toast.makeText(requireContext(), message ?: Message.SOMETHING_HAPPENED, Toast.LENGTH_SHORT).show()
    }
}