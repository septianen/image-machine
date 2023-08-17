package com.septianen.imagemachine.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.septianen.imagemachine.R
import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.databinding.DialogActionBinding
import com.septianen.imagemachine.listener.DialogListener

class ActionDialog(): DialogFragment() {

    constructor(listener: DialogListener, requestCode: Int, title: String, description: String) : this() {
        this.listener = listener
        this.requestCode = requestCode
        this.title = title
        this.description = description
    }

    private lateinit var binding: DialogActionBinding

    private var listener: DialogListener? = null
    private var requestCode: Int? = null
    private var title: String? = null
    private var description: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogActionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setCancelable(false)

        binding.tvTitle.text = title
        binding.tvDescription.text = description

        binding.btnYes.text = when(requestCode) {
            Constant.Dialog.BACK -> getString(R.string.leave)
            Constant.Dialog.DELETE -> getString(R.string.delete)
            else -> getString(R.string.yes)
        }

        binding.ivClose.setOnClickListener {
            requestCode?.let { it1 -> listener?.onCloseDialog(it1) }
        }

        binding.btnYes.setOnClickListener {
            requestCode?.let { it1 -> listener?.onButtonClicked(it1) }
        }
    }
}