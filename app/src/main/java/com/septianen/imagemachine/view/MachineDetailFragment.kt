package com.septianen.imagemachine.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.septianen.imagemachine.adapter.ImageListAdapter
import com.septianen.imagemachine.adapter.MachineListener
import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.databinding.FragmentMachineDetailBinding
import com.septianen.imagemachine.model.Image
import com.septianen.imagemachine.model.Machine
import com.septianen.imagemachine.model.Temporary
import com.septianen.imagemachine.utils.CommonUtil.Companion.convertToInt
import com.septianen.imagemachine.utils.CommonUtil.Companion.convertToString
import com.septianen.imagemachine.utils.DateUtil
import com.septianen.imagemachine.utils.ImageUtil.Companion.getRealPathFromURI
import com.septianen.imagemachine.utils.Resource
import com.septianen.imagemachine.viewmodel.MachineDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineDetailFragment : Fragment(), MachineListener {

    private val viewModel by viewModels<MachineDetailViewModel>()

    private lateinit var binding: FragmentMachineDetailBinding
    private lateinit var machine: Machine

    private var imagePaths: MutableList<String> = ArrayList()

    private val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMachineDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeLiveData()

        binding.btnImage.setOnClickListener {
            openGallery()
        }

        binding.btnDone.setOnClickListener {
            resetErrorMessage()
            updateData()
        }

        binding.tilDate.setOnClickListener {
            datePicker.show(childFragmentManager, datePicker.tag)
        }

        binding.etDate.setOnClickListener {
            datePicker.show(childFragmentManager, datePicker.tag)
        }

        datePicker.addOnPositiveButtonClickListener {

            machine.date = it
            binding.etDate.setText(DateUtil.getDate(it))
        }

        binding.ivDelete.setOnClickListener {
            viewModel.deleteData(machine, imagePaths)
            findNavController().popBackStack()
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

                }
            }
        }

        viewModel.imagesLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    imagePaths = it.data as MutableList<String>

                    updateImage()
                }

                is Resource.Error -> {

                }
            }
        }

        viewModel.saveItemLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { it1 -> showMessage(it1) }
                }

                is Resource.Error -> {
                    when(it.message) {
                        Message.EMPTY_MACHINE_NAME -> binding.tilName.error = it.message
                        Message.EMPTY_MACHINE_TYPE -> binding.tilType.error = it.message
                        Message.EMPTY_MACHINE_NUMBER -> binding.tilNumber.error = it.message
                        Message.EMPTY_MACHINE_DATE -> binding.tilDate.error = it.message
                        Message.EMPTY_IMAGES -> showMessage(it.message)
                        else -> it.message?.let { it1 -> showMessage(it1) }
                    }
                }
            }
        }
    }

    private fun setupView() {
        binding.rvImages.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun updateView() {
        if (machine.id == null) {
            binding.tilId.visibility = View.GONE
        } else {
            binding.tilType.visibility = View.VISIBLE
            binding.etId.setText("${machine.id}")
        }
        binding.etName.setText(machine.name)
        binding.etType.setText(machine.type)
        binding.etNumber.setText(convertToString(machine.qrNumber))
        binding.etDate.setText(DateUtil.getDate(machine.date))
    }

    private fun updateImage() {
        val adapter = ImageListAdapter(this, imagePaths)
        binding.rvImages.adapter = adapter
    }

    private fun resetErrorMessage() {
        binding.tilName.error = null
        binding.tilType.error = null
        binding.tilNumber.error = null
        binding.tilDate.error = null
    }

    private fun updateData() {
        machine.name = convertToString(binding.etName.text.toString())
        machine.type = convertToString(binding.etType.text.toString())
        machine.qrNumber = convertToInt(binding.etNumber.text.toString())

        viewModel.saveData(machine, imagePaths)
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        gallerylauncer.launch(intent)
    }

    private val gallerylauncer =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val data = result.data


            if (data != null) {

                if (data.clipData != null) {
                    val counter = minOf(data.clipData!!.itemCount, 10)

                    for (item in 0 until counter) {

                        val imagePath = getRealPathFromURI(
                            data.clipData!!.getItemAt(item).uri,
                            requireContext()
                        )
                        imagePath?.let { imagePaths.add(it) }
                    }
                } else {
                    val imagePath = data.data?.let {
                        getRealPathFromURI(
                            it,
                            requireContext()
                        )
                    }
                    imagePath?.let { imagePaths.add(it) }
                }

                updateImage()
            }
        }

    override fun onItemClicked(position: Int) {
        val intent = Intent(requireContext(), ImagePreviewActivity::class.java)
        Temporary.imagePath = imagePaths[position]
        imagePreviewlauncer.launch(intent)
    }

    private val imagePreviewlauncer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        // RESULT_OK if user click delete button
        if (result.resultCode == RESULT_OK) {

            viewModel.deleteImage(Temporary.imagePath)

            showMessage(Message.SUCCESS_DELETE_IMAGE)
        }
    }
}