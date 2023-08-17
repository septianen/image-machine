package com.septianen.imagemachine.adapter

interface MachineListener {

    fun onItemClicked(position: Int)

    fun onItemLongClicked(positions: List<Int>)
}