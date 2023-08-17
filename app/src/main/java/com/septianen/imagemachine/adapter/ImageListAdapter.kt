package com.septianen.imagemachine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.septianen.imagemachine.R
import com.septianen.imagemachine.listener.MachineListener
import com.septianen.imagemachine.model.Image


class ImageListAdapter(
    private val listener: MachineListener,
    private val images: List<Image>
): RecyclerView.Adapter<ImageListAdapter.ViewHolder>(){

    private lateinit var context: Context

    private var isImageSelected = false
    private var selectedPositions = ArrayList<Int>()

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
            .load(images[position].imagePath)
            .centerCrop()
            .placeholder(R.drawable.baseline_qr_code_scanner_24)
            .into(holder.ivMachine);

        holder.itemView.setOnClickListener {
            if (isImageSelected) {
                if (selectedPositions.contains(position)) {
                    selectedPositions.remove(position)
                    holder.ivMachine.background = null
                } else {
                    selectedPositions.add(position)
                    holder.ivMachine.background = context.getDrawable(R.color.primary)
                }

                listener.onItemLongClicked(selectedPositions)
            } else {
                listener.onItemClicked(position)
            }
        }

        holder.itemView.setOnLongClickListener(OnLongClickListener {

            isImageSelected = true

            if (!selectedPositions.contains(position))
                selectedPositions.add(position)

            holder.ivMachine.background = context.getDrawable(R.color.primary)
            listener.onItemLongClicked(selectedPositions)
            true
        })
    }

}