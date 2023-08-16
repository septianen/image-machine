package com.septianen.imagemachine.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.septianen.imagemachine.R
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.databinding.FragmentMachineDetailBinding
import com.septianen.imagemachine.model.Machine
import com.septianen.imagemachine.utils.CommonUtil.Companion.convertToString
import com.septianen.imagemachine.utils.Resource
import com.septianen.imagemachine.viewmodel.MachineDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineDetailFragment : Fragment() {

    private val viewModel by viewModels<MachineDetailViewModel>()

    private lateinit var binding: FragmentMachineDetailBinding

    private lateinit var machine: Machine

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMachineDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()


        binding.btnDone.setOnClickListener {
            resetErrorMessage()
            updateData()
        }
    }

    private fun observeLiveData(){
        viewModel.machineLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    machine = it.data ?: Machine()
                    updateView()
                }

                is Resource.Error -> {
                    when(it.message) {
                        Message.EMPTY_MACHINE_NAME -> binding.tilName.error = it.message
                        Message.EMPTY_MACHINE_TYPE -> binding.tilType.error = it.message
                        else -> it.message?.let { it1 -> showMessage(it1) }
                    }
                }
            }
        }
    }

    private fun updateView() {
        binding.etName.setText(machine.name)
        binding.etType.setText(machine.type)
    }

    private fun resetErrorMessage() {
        binding.tilName.error = null
        binding.tilType.error = null
    }

    private fun updateData() {
        machine.name = convertToString(binding.etName.text.toString())
        machine.type = convertToString(binding.etType.text.toString())

        viewModel.saveData(machine)
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

    }

    private val gallerylauncer =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val data = result.data

            

        }
}