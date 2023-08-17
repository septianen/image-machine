package com.septianen.imagemachine.view

import android.os.Bundle
import android.util.Log
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
import com.septianen.imagemachine.listener.MachineListener
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.databinding.FragmentMachineListBinding
import com.septianen.imagemachine.dialog.SortDialog
import com.septianen.imagemachine.listener.SortListener
import com.septianen.imagemachine.model.Machine
import com.septianen.imagemachine.model.Temporary
import com.septianen.imagemachine.utils.Resource
import com.septianen.imagemachine.viewmodel.MachineListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MachineListFragment : Fragment(), MachineListener, SortListener {


    private val viewModel by viewModels<MachineListViewModel>()
    private val sortDialog by lazy {
        SortDialog(this)
    }

    private lateinit var binding: FragmentMachineListBinding

    private var machines: List<Machine>? = ArrayList()
    private var adapter: MachineListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivSort.setOnClickListener {
            sortDialog.show(childFragmentManager, sortDialog.tag)
        }
    }


    private fun observeLiveData() {
        viewModel.machineLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    machines = it.data ?: ArrayList()

                    updateView()
                }

                is Resource.Error -> {
                    showMessage(it.message)

                    machines = ArrayList()
                    updateView()
                }
            }
        }
    }

    private fun setupView() {
        adapter = MachineListAdapter(this)
        binding.rvMachine.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvMachine.adapter = adapter
    }

    private fun updateView() {

        machines?.let { adapter?.setData(it) }

        if (machines.isNullOrEmpty()) {
            binding.rvMachine.visibility = View.GONE
        } else {
            binding.rvMachine.visibility = View.VISIBLE
        }
    }

    private fun showMessage(message: String?) {
        Toast.makeText(requireContext(), message ?: Message.SOMETHING_HAPPENED, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(position: Int) {
        Temporary.machine = machines?.get(position)?.copy()
        openNextPage()
    }

    override fun onItemLongClicked(positions: List<Int>) {

    }

    override fun onResume() {
        super.onResume()

        viewModel.getMachines()
    }

    override fun onSort(category: Int, sort: Int) {

        sortDialog.dismiss()

        viewModel.sortMachines(category, sort)
    }

    private fun openNextPage() {
        Temporary.image = null
        findNavController().navigate(R.id.openMachineDetail)
    }
}