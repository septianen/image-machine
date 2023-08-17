package com.septianen.imagemachine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.septianen.imagemachine.R
import com.septianen.imagemachine.model.Image
import com.septianen.imagemachine.model.Machine

class ImageListAdapter(
    private val listener: MachineListener,
    private val images: List<String>
): RecyclerView.Adapter<ImageListAdapter.ViewHolder>(){

    private lateinit var context: Context

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val ivMachine: ImageView

        init {
            ivMachine = view.findViewById(R.id.iv_machine_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Glide
            .with(context)
            .load(images[position])
            .centerCrop()
            .placeholder(R.drawable.baseline_qr_code_scanner_24)
            .into(holder.ivMachine);

        holder.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

}