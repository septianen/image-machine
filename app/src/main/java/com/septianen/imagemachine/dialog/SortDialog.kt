package com.septianen.imagemachine.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.databinding.DialogSortBinding
import com.septianen.imagemachine.listener.SortListener

class SortDialog(): DialogFragment() {

    constructor(listener: SortListener) : this() {
        this.listener = listener
    }

    private lateinit var binding: DialogSortBinding

    private var listener: SortListener? = null

    private var category = Constant.Sort.NAME
    private var sort = Constant.Sort.ASC

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogSortBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setCancelable(false)

        binding.rgCategory.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                binding.rbName.id -> category = Constant.Sort.NAME
                binding.rbType.id -> category = Constant.Sort.TYPE
            }
        }

        binding.rgSort.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                binding.rbAscending.id -> sort = Constant.Sort.ASC
                binding.rbDescending.id -> sort = Constant.Sort.DSC
            }
        }

        binding.ivClose.setOnClickListener {
            dialog?.dismiss()
        }

        binding.btnYes.setOnClickListener {
            listener?.onSort(category, sort)
        }
    }
}