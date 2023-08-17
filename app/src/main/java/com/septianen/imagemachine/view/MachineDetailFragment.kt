package com.septianen.imagemachine.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.septianen.imagemachine.R
import com.septianen.imagemachine.adapter.ImageListAdapter
import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.listener.MachineListener
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.databinding.FragmentMachineDetailBinding
import com.septianen.imagemachine.dialog.ActionDialog
import com.septianen.imagemachine.listener.DialogListener
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

class MachineDetailFragment : Fragment(), MachineListener, DialogListener {

    private val backDialog by lazy {
        ActionDialog(
            this,
            Constant.Dialog.BACK,
            Message.Dialog.LEAVE,
            Message.Dialog.PLEASE_SAVE_DATA
        )
    }

    private val deleteDialog by lazy {
        ActionDialog(
            this,
            Constant.Dialog.DELETE,
            Message.Dialog.DELETE,
            Message.Dialog.DELETED_DATA_WILL_LOST
        )
    }

    private val viewModel by viewModels<MachineDetailViewModel>()

    private lateinit var binding: FragmentMachineDetailBinding
    private lateinit var machine: Machine

    private var imagePaths: MutableList<Image> = ArrayList()

    private val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()

    private var isImageSelected = false
    private var selectedPositions: List<Int> = ArrayList()

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

        binding.ivBack.setOnClickListener {
            backDialog.show(parentFragmentManager, backDialog.tag)
        }

        binding.btnImage.setOnClickListener {
            if (isImageSelected) {
                deleteSelectedImages()
            } else {
                openGallery()
            }
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
            deleteDialog.show(parentFragmentManager, deleteDialog.tag)
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
                    imagePaths = it.data as MutableList<Image>

                    Log.d("TAG", "V imagesLiveData: imagePaths = ${imagePaths.size}")


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
                    isImageSelected = false
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

    private fun deleteSelectedImages() {
        if (isImageSelected) {
            var deletedImages = ArrayList<Image>()
            for (position in selectedPositions) {
                deletedImages.add(imagePaths[position])
            }
            imagePaths.removeAll(deletedImages)
        }

        updateImage()
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
                    val counter = viewModel.countMaximumImage(data.clipData!!.itemCount, imagePaths.size)

                    for (item in 0 until counter) {

                        val imagePath = getRealPathFromURI(
                            data.clipData!!.getItemAt(item).uri,
                            requireContext()
                        )
                        imagePath?.let { imagePaths.add(
                            Image(
                                imagePath = imagePath
                            )
                        ) }
                    }
                } else {
                    val imagePath = data.data?.let {
                        getRealPathFromURI(
                            it,
                            requireContext()
                        )
                    }
                    imagePath?.let { imagePaths.add(
                        Image(
                            imagePath = imagePath
                        )
                    ) }
                }


                Log.d("TAG", "V gallerylauncer: imagePaths = ${imagePaths.size}")

                updateImage()
            }
        }

    override fun onItemClicked(position: Int) {
        val intent = Intent(requireContext(), ImagePreviewActivity::class.java)
        Temporary.image = imagePaths[position]
        imagePreviewlauncer.launch(intent)
    }

    override fun onItemLongClicked(positions: List<Int>) {

        if (positions.isEmpty()) {
            isImageSelected = false
            binding.btnImage.text = getString(R.string.machine_image)
        } else {
            isImageSelected = true
            selectedPositions = positions
            binding.btnImage.text = getString(R.string.delete_image)
        }
    }

    private val imagePreviewlauncer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        // RESULT_OK if user click delete button
        if (result.resultCode == RESULT_OK) {

            imagePaths.remove(Temporary.image)
            updateImage()
        }
    }

    override fun onCloseDialog(requestCode: Int) {
        when (requestCode) {
            Constant.Dialog.BACK -> backDialog.dismiss()
            Constant.Dialog.DELETE -> deleteDialog.dismiss()
        }
    }

    override fun onButtonClicked(requestCode: Int) {
        when (requestCode) {
            Constant.Dialog.BACK -> backDialog.dismiss()
            Constant.Dialog.DELETE -> {
                deleteDialog.dismiss()
                viewModel.deleteData(machine, imagePaths)
            }
        }
        findNavController().popBackStack()
    }
}