package com.septianen.imagemachine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.septianen.imagemachine.R
import com.septianen.imagemachine.model.Machine

class MachineListAdapter(
    private val listener: MachineListener,
    private val machines: List<Machine>
): RecyclerView.Adapter<MachineListAdapter.ViewHolder>(){
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val tvName: TextView
        val tvType: TextView

        init {
            tvName = view.findViewById(R.id.tv_name)
            tvType = view.findViewById(R.id.tv_type)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_machine, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return machines.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = machines[position].name
        holder.tvType.text = machines[position].type

        holder.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

}