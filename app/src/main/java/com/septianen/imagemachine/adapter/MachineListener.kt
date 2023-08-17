package com.septianen.imagemachine.adapter

import android.view.View

interface MachineListener {

    fun onItemClicked(position: Int)

    fun onItemLongClicked(view: View, position: Int)
}