package com.septianen.imagemachine.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.septianen.imagemachine.R
import com.septianen.imagemachine.listener.MachineListener
import com.septianen.imagemachine.model.Machine

class MachineListAdapter(
    private val listener: MachineListener,
    private var machines: List<Machine>? = null
): RecyclerView.Adapter<MachineListAdapter.ViewHolder>(){

    private lateinit var context: Context

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val tvName: TextView
        val tvType: TextView
        val ivThumbnail: ImageView

        init {
            tvName = view.findViewById(R.id.tv_name)
            tvType = view.findViewById(R.id.tv_type)
            ivThumbnail = view.findViewById(R.id.iv_thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_machine, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return machines?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = machines?.get(position)?.name ?: ""
        holder.tvType.text = machines?.get(position)?.type ?: ""

        Glide
            .with(context)
            .load(machines?.get(position)?.thumbnail)
            .circleCrop()
            .placeholder(R.drawable.baseline_broken_image_24)
            .into(holder.ivThumbnail)

        holder.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

    fun setData(newMachines: List<Machine>) {
        this.machines = newMachines
        notifyDataSetChanged()
    }
}