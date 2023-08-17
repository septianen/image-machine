package com.septianen.imagemachine.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.septianen.imagemachine.R
import com.septianen.imagemachine.adapter.MachineListAdapter
import com.septianen.imagemachine.adapter.MachineListener
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.databinding.FragmentMachineListBinding
import com.septianen.imagemachine.model.Machine
import com.septianen.imagemachine.model.Temporary
import com.septianen.imagemachine.utils.Resource
import com.septianen.imagemachine.viewmodel.MachineListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineListFragment : Fragment(), MachineListener {


    private val viewModel by viewModels<MachineListViewModel>()

    private lateinit var binding: FragmentMachineListBinding

    private lateinit var machines: List<Machine>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMachineListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeLiveData()

        binding.btnAdd.setOnClickListener {
            Temporary.machine = null
            Temporary.images = null
            openNextPage()
        }
    }


    private fun observeLiveData() {
        viewModel.machineLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    showMessage(it.data.toString())
                    machines = it.data ?: ArrayList()
                    updateView()
                }

                is Resource.Error -> {
                    showMessage(it.message)
                }
            }
        }
    }

    private fun setupView() {
        binding.rvMachine.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    private fun updateView() {
        val adapter = MachineListAdapter(this, machines)
        binding.rvMachine.adapter = adapter
    }

    private fun showMessage(message: String?) {
        Toast.makeText(requireContext(), message ?: Message.SOMETHING_HAPPENED, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(position: Int) {
        Temporary.machine = machines[position].copy()
        openNextPage()
    }

    private fun openNextPage() {
        Temporary.imagePath = null
        findNavController().navigate(R.id.openMachineDetail)
    }
}