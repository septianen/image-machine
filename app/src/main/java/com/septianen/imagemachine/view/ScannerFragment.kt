package com.septianen.imagemachine.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.septianen.imagemachine.R
import com.septianen.imagemachine.databinding.FragmentScannerBinding
import com.septianen.imagemachine.model.Temporary
import com.septianen.imagemachine.utils.CommonUtil.Companion.convertToInt
import com.septianen.imagemachine.utils.Resource
import com.septianen.imagemachine.viewmodel.ScannerlViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding
    private lateinit var codeScanner: CodeScanner

    private val viewmodel by viewModels<ScannerlViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScannerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()

        codeScanner = CodeScanner(requireActivity(), binding.scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                viewmodel.getData(convertToInt(it.text))
            }
        }

        binding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun observeLiveData() {
        viewmodel.machineLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    Temporary.machine = it.data
                    findNavController().navigate(R.id.openMachineDetail)
                    viewmodel.machineLiveData.postValue(null)
                }

                is Resource.Error -> {
                    showMessage(it.message)
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun showMessage(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}